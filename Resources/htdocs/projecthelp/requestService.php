<?php require_once('Connections/connDB.php'); ?>
<?php
$response = array();

$input = file_get_contents("php://input");
$jsonObj = json_decode($input, true);
$requesteeId = $jsonObj['id'];
$name = $jsonObj['name'];
$gender = $jsonObj['gender'];
$serviceType = $jsonObj['serviceType'];
$address = $jsonObj['address'];
$unitno = $jsonObj['unitno'];
$lat = $jsonObj['lat'];
$long = $jsonObj['long'];
$requestDate = $jsonObj['requestDate'];
$requestType = $jsonObj['requestTime'];
$msgType = $jsonObj['msgType']; // MsgType

	$query_rsInsertRequest = sprintf("INSERT INTO request (requesteeId, requestee, gender, type, address, unitno, locationLat, locationLong, requestDate, requestTime) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", $requesteeId, $name, $gender, $serviceType, $address, $unitno, $lat, $long, $requestDate, $requestType);

	$rsInsertRequest = mysqli_query($connDB, $query_rsInsertRequest);
	$rsID = mysqli_insert_id($connDB);

	$response["type"] = $msgType;
	$response["name"] = $name;
	$response["gender"] = $gender;
	$response["address"] = $address;
	$response["sql"] = $query_rsInsertRequest;

	if ($rsInsertRequest != 0)
	{
			$response["status"] = "OK";
	}
	else
	{
		$response["status"] = "NOK";
	}

echo json_encode($response);

mysqli_close($connDB);
?>
