import React, { useState, useEffect, useRef } from "react";
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  Image,
} from "react-native";
import MapView, { Marker, Polyline } from "react-native-maps";
import * as Location from "expo-location";
import CustomMarker from "./work";
import {
  GooglePlaceData,
  GooglePlaceDetail,
  GooglePlacesAutocomplete,
  GooglePlacesAutocompleteRef,
} from "react-native-google-places-autocomplete";
import Icon from "react-native-vector-icons/Ionicons";
import MaterialIcons from "react-native-vector-icons/MaterialIcons";
import {
  ApiRouteRequestInput,
  ApiRouteRequestOutput,
  Edge,
  Node,
} from "@/types/map";
import { findCurrentEdge } from "@/utils/map";
import { GOOGLE_API_KEY } from "./api";

interface PlaceDetails {
  name: string;
  address: string;
}

const DEFAULT_DELTA = 0.005;

const DEFAULT_LOCATION = {
  latitude: 1.2916692453498382,
  longitude: 103.84946752679058,
  latitudeDelta: DEFAULT_DELTA,
  longitudeDelta: DEFAULT_DELTA,
};

export default function App() {
  const [selectedLocation, setSelectedLocation] =
    useState<typeof DEFAULT_LOCATION>();
  const [userLocation, setUserLocation] = useState(DEFAULT_LOCATION);
  const [errorMessage, setErrorMsg] = useState("");
  const [placeDetails, setPlaceDetails] = useState<PlaceDetails | null>(null);
  const searchBarRef = useRef<GooglePlacesAutocompleteRef>(null);
  const mapRef = useRef<MapView>(null);
  const [inRange, setInRange] = useState(true);
  const [route, setRoute] = useState<ApiRouteRequestOutput | null>(null);
  const [currentEdge, setCurrentEdge] = useState<Edge | null>(null);
  const [passedEdgeIds, setPassedEdgeIds] = useState<string[]>([]);
  const [requestLoading, setRequestLoading] = useState(false);
  const [destinationPage, setDestinationPage] = useState(false);

  const [startingPoint, setStartingPoint] = useState<{
    lat: number;
    lon: number;
  }>();
  const [endPoint, setEndPoint] = useState<{ lat: number; lon: number }>();

  function getDistanceFromLatLonInKm(
    lat1: number,
    lon1: number,
    lat2: number,
    lon2: number
  ) {
    const R = 6371; // Radius of the Earth in km
    const dLat = (lat2 - lat1) * (Math.PI / 180);
    const dLon = (lon2 - lon1) * (Math.PI / 180);
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1 * (Math.PI / 180)) *
        Math.cos(lat2 * (Math.PI / 180)) *
        Math.sin(dLon / 2) *
        Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const d = R * c; // Distance in km
    return d;
  }

  const handleRouteRequest = async (input: ApiRouteRequestInput) => {
    setRequestLoading(true);
    const res = await fetch(
      `https://walkthru.osiris.sg/nodes?startlat=${input.startPoint.lat}&startlon=${input.startPoint.lon}&endlat=${input.endPoint.lat}&endlon=${input.endPoint.lon}`,
      {
        method: "get",
      }
    );
    const data = (await res.json()) as ApiRouteRequestOutput;
    console.log(data);
    setRoute(data);
    const edges = data.edges
      .filter((edge) => edge.fromNodeId !== data.startNodeId)
      .map((edge) => edge.edgeId);
    setPassedEdgeIds(edges);
    const startingNode = data.nodes.find(
      (node) => node.nodeId == data.startNodeId
    );
    zoomIn(input.startPoint.lat, input.startPoint.lon);
    setRequestLoading(false);
  };

  const getUserLocation = async () => {
    // if using real time location
    // let { status } = await Location.requestForegroundPermissionsAsync();
    // if (status !== "granted") {
    //   setErrorMsg("Permission to access location was denied");
    // }
    // let location = await Location.getCurrentPositionAsync({});
    // const newLocation = {
    //   latitude: location.coords.latitude,
    //   longitude: location.coords.longitude,
    //   latitudeDelta: DEFAULT_DELTA,
    //   longitudeDelta: DEFAULT_DELTA,
    // };

    // used for demo as fixed location within the scope
    const newLocation = {
      latitude: 1.2916692453498382,
      longitude: 103.84946752679058,
      latitudeDelta: DEFAULT_DELTA,
      longitudeDelta: DEFAULT_DELTA,
    };
    setUserLocation(newLocation);
    setPlaceDetails(null);

    zoomIn(newLocation.latitude, newLocation.longitude);

    if (searchBarRef.current) {
      searchBarRef.current.clear();
    }
    setInRange(true);
  };

  const zoomIn = (lat: number, lon: number) => {
    if (mapRef.current) {
      mapRef.current.animateToRegion(
        {
          latitude: lat,
          longitude: lon,
          latitudeDelta: DEFAULT_DELTA,
          longitudeDelta: DEFAULT_DELTA,
        },
        1000
      );
    }
    setSelectedLocation(undefined);
    setInRange(true);
  };

  useEffect(() => {
    // if we want live user location
    //   const requestPermissions = async () => {
    //     const watchPosition = await Location.watchPositionAsync(
    //       {
    //         accuracy: Location.Accuracy.High,
    //         timeInterval: 5000, // update every 5 seconds
    //         distanceInterval: 1, // update when the user moves by 1 meter
    //       },
    //       (location) => {
    //         const { latitude, longitude } = location.coords;
    //         setUserLocation({
    //           latitude,
    //           longitude,
    //           latitudeDelta: 0.00005,
    //           longitudeDelta: 0.00005,
    //         });
    //         if (!!route) {
    //           const edge = findCurrentEdge(latitude, longitude, route);
    //           if (edge && edge !== currentEdge) {
    //             setCurrentEdge(edge);
    //             updatePassedEdges(edge, route);
    //           }
    //         }
    //       }
    //     );
    //     // Cleanup function to stop watching the location
    //     return () => {
    //       watchPosition.remove();
    //     };
    //   };
    //   getUserLocation();
    //   requestPermissions();
    // }, []);
  });

  const handlePlaceSelect = (
    data: GooglePlaceData,
    details: GooglePlaceDetail | null
  ) => {
    if (!details) {
      return;
    }

    // Define the central location and radius
    const centralLocation = {
      latitude: 1.293302397264968,
      longitude: 103.85519355229445,
    };
    const radiusInKm = 0.8; // 800m radius

    // Calculate the distance of the selected place from the central location
    const distance = getDistanceFromLatLonInKm(
      centralLocation.latitude,
      centralLocation.longitude,
      details.geometry.location.lat,
      details.geometry.location.lng
    );

    // Filter places outside the defined radius
    if (distance < radiusInKm) {
      setInRange(true);
    } else {
      setInRange(false);
    }

    const newLocation = {
      latitude: details.geometry.location.lat,
      longitude: details.geometry.location.lng,
      latitudeDelta: DEFAULT_DELTA,
      longitudeDelta: DEFAULT_DELTA,
    };

    setSelectedLocation(newLocation);
    setPlaceDetails({
      name: details.name,
      address: details.formatted_address,
    });

    if (mapRef.current) {
      mapRef.current.animateToRegion(newLocation, 1000);
    }
  };

  const clearSearch = () => {
    if (searchBarRef.current) {
      console.log("Clearing search bar");
      searchBarRef.current.clear();
    }
  };

  const destinationSelector = () => {
    setDestinationPage(true);
  };

  const destinationDeSelector = () => {
    setDestinationPage(false);
  };

  const updatePassedEdges = (
    currentEdge: Edge,
    apiRoute: ApiRouteRequestOutput
  ) => {
    if (!currentEdge) return;

    const { nodes, edges } = apiRoute;

    let edgeSequence = [];
    let currentNode = nodes.find(
      (node) => node.nodeId === apiRoute.startNodeId
    );
    if (!currentNode) return;

    while (currentNode?.nodeId !== currentEdge.fromNodeId) {
      const nextEdge = edges.find(
        (edge) =>
          edge.fromNodeId === currentNode?.nodeId &&
          !passedEdgeIds.includes(edge.edgeId)
      );
      if (!nextEdge) break;
      edgeSequence.push(nextEdge.edgeId);
      currentNode = nodes.find((node) => node.nodeId === nextEdge.toNodeId);
    }

    if (currentNode?.nodeId === currentEdge.fromNodeId) {
      setPassedEdgeIds([...passedEdgeIds, ...edgeSequence]);
    }
  };

  return (
    <View style={styles.container}>
      {errorMessage ? (
        <Text style={styles.errorText}>{errorMessage}</Text>
      ) : null}
      {!selectedLocation && !destinationPage && (
        <View style={styles.searchContainer}>
          <GooglePlacesAutocomplete
            ref={searchBarRef}
            placeholder="Search"
            fetchDetails={true}
            onPress={(data, details = null) => handlePlaceSelect(data, details)}
            query={{
              key: "GOOGLE_API_KEY",
              language: "en",
              components: "country:sg",
            }}
            styles={{
              textInputContainer: styles.textInputContainer,
              textInput: styles.textInput,
              listView: styles.listView,
            }}
          />
          <TouchableOpacity style={styles.clearButton} onPress={clearSearch}>
            <Icon name="close-circle" size={26} color="gray" />
          </TouchableOpacity>
        </View>
      )}
      <MapView ref={mapRef} style={styles.map} initialRegion={DEFAULT_LOCATION}>
        <Marker coordinate={userLocation}>
          <CustomMarker />
        </Marker>
        {placeDetails && selectedLocation && (
          <Marker coordinate={selectedLocation} />
        )}
        {/* {!!route &&
          route.nodes.map((node, index) => (
            <Marker
              coordinate={{ latitude: node.lat, longitude: node.lon }}
              key={index}
            />
          ))} */}
        {route?.edges.map((edge, index) => {
          const startNode = route.nodes.find(
            (node) => node.nodeId === edge.fromNodeId
          );
          const endNode = route.nodes.find(
            (node) => node.nodeId === edge.toNodeId
          );
          if (!startNode || !endNode) return null;

          let strokeColor = "#000000"; // Default color

          if (passedEdgeIds.includes(edge.edgeId)) {
            strokeColor = "#808080"; // Current path color
          } else {
            strokeColor = "#1E90FF"; // Previous path color
          }

          return (
            <Polyline
              key={index}
              coordinates={[
                { latitude: startNode.lat, longitude: startNode.lon },
                { latitude: endNode.lat, longitude: endNode.lon },
              ]}
              strokeColor={strokeColor}
              strokeWidth={6}
            />
          );
        })}
      </MapView>
      <TouchableOpacity
        style={
          inRange
            ? !selectedLocation
              ? styles.myLocationButton
              : styles.myLocationButtonDetails
            : styles.myLocationButtonOutOfRange
        }
        onPress={() => zoomIn(userLocation.latitude, userLocation.longitude)}
      >
        <Icon name="location" size={24} color="black" />
      </TouchableOpacity>
      <TouchableOpacity
        style={styles.navigationButton}
        onPress={destinationSelector}
      >
        <Icon name="navigate" size={26} color="black" />
      </TouchableOpacity>
      {destinationPage && (
        <View style={styles.navigationContainer}>
          <View
            style={{
              display: "flex",
              flexDirection: "row",
              justifyContent: "flex-start",
              alignItems: "center",
              gap: 16,
              marginBottom: 16,
            }}
          >
            <TouchableOpacity
              style={{
                aspectRatio: 1,
              }}
              onPress={
                // async () =>
                // await handleRouteRequest({
                //   startPoint: {
                //     lat: userLocation.latitude,
                //     lon: userLocation.longitude,
                //   },
                //   endPoint: {
                //     lat: 1.2939497673038174,
                //     lon: 103.85564168210729,
                //   },
                // })
                () => destinationDeSelector()
              }
            >
              <Icon name="arrow-back" size={22} color="black" />
            </TouchableOpacity>
            <Text style={styles.navigationSelect}>Select Destination</Text>
          </View>
          <View
            style={{
              display: "flex",
              flexDirection: "row",
              justifyContent: "flex-start",
              alignItems: "center",
              gap: 16,
              marginBottom: 16,
            }}
          >
            <CustomMarker />
            <GooglePlacesAutocomplete
              ref={searchBarRef}
              placeholder="Starting Point"
              fetchDetails={true}
              onPress={(_, detail) =>
                setStartingPoint({
                  lat:
                    detail?.geometry.location.lat ?? DEFAULT_LOCATION.latitude,
                  lon:
                    detail?.geometry.location.lng ?? DEFAULT_LOCATION.longitude,
                })
              }
              query={{
                key: "GOOGLE_API_KEY",
                language: "en",
                components: "country:sg",
              }}
              styles={{
                textInputContainer: styles.searchInput,
                textInput: styles.textInput1,
                listView: styles.listView1,
              }}
            />
          </View>
          <View
            style={{
              display: "flex",
              flexDirection: "row",
              justifyContent: "flex-start",
              alignItems: "center",
              gap: 16,
              marginBottom: 16,
            }}
          >
            <MaterialIcons name="place" size={40} color={"red"} />
            <GooglePlacesAutocomplete
              ref={searchBarRef}
              placeholder="End Point"
              fetchDetails={true}
              onPress={(_, detail) =>
                setEndPoint({
                  lat:
                    detail?.geometry.location.lat ?? DEFAULT_LOCATION.latitude,
                  lon:
                    detail?.geometry.location.lng ?? DEFAULT_LOCATION.longitude,
                })
              }
              query={{
                key: GOOGLE_API_KEY,
                language: "en",
                components: "country:sg",
              }}
              styles={{
                textInputContainer: styles.searchInput,
                textInput: styles.textInput1,
                listView: styles.listView2,
              }}
            />
          </View>
          <View>
            <TouchableOpacity
              style={{
                width: "100%",
                backgroundColor: !startingPoint || !endPoint ? "gray" : "green",
                padding: 10,
                borderRadius: 10,
              }}
              onPress={async () => {
                if (!!startingPoint && !!endPoint) {
                  await handleRouteRequest({
                    startPoint: startingPoint,
                    endPoint: endPoint,
                  });
                }
              }}
              disabled={!startingPoint || !endPoint || requestLoading}
            >
              <Text
                style={{
                  textAlign: "center",
                  color: "white",
                  fontSize: 20,
                  fontWeight: "bold",
                }}
              >
                Go
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      )}
      {placeDetails && inRange && selectedLocation && (
        <View style={styles.detailsContainer}>
          <Text style={styles.placeName}>{placeDetails.name}</Text>
          <Text style={styles.placeAddress}>{placeDetails.address}</Text>
          <ScrollView
            horizontal
            showsHorizontalScrollIndicator={false}
            style={styles.imageScrollView}
          >
            <Image
              source={require("../app/images/images.jpg")}
              style={styles.image}
            />
            <Image
              source={require("../app/images/images.jpg")}
              style={styles.image}
            />
            <Image
              source={require("../app/images/images.jpg")}
              style={styles.image}
            />
            <Image
              source={require("../app/images/images.jpg")}
              style={styles.image}
            />
            <Image
              source={require("../app/images/images.jpg")}
              style={styles.image}
            />
          </ScrollView>
        </View>
      )}
      {!inRange && (
        <View style={styles.detailsContainer}>
          <Text style={styles.errorText}>
            The selected place has not been implemented yet.
          </Text>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
  searchInput: {
    padding: 1,
    borderWidth: 1,
    borderColor: "black",
    borderRadius: 10,
    backgroundColor: "white",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.8,
    shadowRadius: 2,
    elevation: 5,
  },
  textInputContainer1: {
    width: "100%",
    position: "relative",
  },
  textInput1: {
    height: 44,
    color: "#5d5d5d",
    fontSize: 18,
    textAlign: "left",
    position: "relative",
  },
  listView1: {
    position: "absolute",
    top: 210,
    backgroundColor: "white",
  },
  listView2: {
    position: "absolute",
    top: 120,
    backgroundColor: "white",
  },
  markerNavigation: {
    position: "absolute",
    top: 130,
    left: 24,
  },
  searchContainer: {
    position: "absolute",
    top: 10,
    width: "100%",
    zIndex: 1,
    paddingHorizontal: 12,
  },
  textInputContainer: {
    width: "100%",
    position: "relative",
  },
  textInput: {
    height: 44,
    color: "#5d5d5d",
    fontSize: 20,
    textAlign: "left",
    paddingRight: 40,
  },
  clearButton: {
    position: "absolute",
    right: 17,
    top: 8.5,
  },
  navigationButton: {
    position: "absolute",
    bottom: 80,
    right: 20,
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: "white",
    justifyContent: "center",
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.8,
    shadowRadius: 2,
    elevation: 5,
  },
  navigationBack: {
    position: "absolute",
    top: 18,
    left: 12,
  },
  customNavigation: {
    position: "absolute",
    top: 60,
    left: 24,
  },
  navigationContainer: {
    position: "absolute",
    top: 0,
    backgroundColor: "white",
    padding: 15,
    width: "100%",
    alignSelf: "center",
    height: "auto",
  },
  navigationSelect: {
    fontSize: 20,
    fontWeight: "bold",
  },
  searchNavigation: {
    position: "absolute",
    top: 10,
    width: "70%",
    zIndex: 1,
    paddingHorizontal: 12,
  },
  myLocationButton: {
    position: "absolute",
    bottom: 20,
    right: 20,
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: "white",
    justifyContent: "center",
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.8,
    shadowRadius: 2,
    elevation: 5,
  },
  myLocationButtonDetails: {
    position: "absolute",
    bottom: 260,
    right: 20,
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: "white",
    justifyContent: "center",
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.8,
    shadowRadius: 2,
    elevation: 5,
  },
  myLocationButtonOutOfRange: {
    position: "absolute",
    bottom: 90,
    right: 20,
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: "white",
    justifyContent: "center",
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.8,
    shadowRadius: 2,
    elevation: 5,
  },
  listView: {
    backgroundColor: "white",
  },
  errorText: {
    color: "red",
    textAlign: "center",
    marginVertical: 10,
  },
  detailsContainer: {
    position: "absolute",
    bottom: 0,
    backgroundColor: "white",
    padding: 15,
    width: "100%",
    alignSelf: "center",
  },
  placeName: {
    fontSize: 18,
    fontWeight: "bold",
  },
  placeAddress: {
    fontSize: 16,
    marginTop: 5,
  },
  imageScrollView: {
    marginTop: 10,
  },
  image: {
    width: 150,
    height: 150,
    marginRight: 10,
    borderRadius: 10,
  },
});
