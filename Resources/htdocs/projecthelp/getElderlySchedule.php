<?php require_once ("Connections/connDB.php") ?>
<?php
$response = array();

$input = file_get_contents("php://input");
$jsonObj = json_decode($input, true);
$requestType = $jsonObj['msgType'];
$currentUser = $jsonObj['id'];

$query_rsGetRequest = sprintf("SELECT * FROM request WHERE requesteeId = '%s'", $currentUser);
$rsRequests = mysqli_query($connDB, $query_rsGetRequest);
$response["msgType"] = $requestType;

if(mysqli_num_rows($rsRequests) > 0){
	while ($row_RequestData = mysqli_fetch_assoc($rsRequests)) {
    $response["elderlyScheduledDetails"][] = array(
    "requestId" => $row_RequestData["requestId"], // First from json, $row_RequestData is from db
    "requestee" => $row_RequestData["requestee"],
    "type" => $row_RequestData["type"],
    "gender" => $row_RequestData["gender"],
    "address" => $row_RequestData["address"],
    "unitno" => $row_RequestData["unitno"],
    "locationLat" => $row_RequestData["locationLat"],
    "locationLong" => $row_RequestData["locationLong"],
    "requestDate" => $row_RequestData["requestDate"],
    "requestTime" => $row_RequestData["requestTime"],
    "requestStatus" => $row_RequestData['status']);
  }
  $response["status"] = "OK";
}
else {
	$response["status"] = "NOK";
}

echo json_encode($response);

mysqli_close($connDB);
?>
