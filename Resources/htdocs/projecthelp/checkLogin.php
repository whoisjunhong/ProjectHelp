<?php require_once('Connections/connDB.php'); ?>
<?php
$response = array();

$input = file_get_contents("php://input");
$jsonObj = json_decode($input, true);
$id = $jsonObj['id'];
$checkLogin = $jsonObj['checkLogin'];
$requestType = $jsonObj['type'];

$query_verifyUser = sprintf("SELECT * FROM profile WHERE id = %s AND checklogin = '%s'", $id, $checkLogin);
$verifyUser = mysqli_query($connDB, $query_verifyUser);

$response["type"] = $requestType;

if(mysqli_num_rows($verifyUser) > 0){
	$row_verifyUser = mysqli_fetch_assoc($verifyUser);
	$response["username"] = $row_verifyUser['username'];
	$response["name"] = $row_verifyUser['name'];
	$response["usertype"] = $row_verifyUser['usertype'];
	$response["status"] = "OK";
}
else {
	$response["status"] = "NOK";
}

echo json_encode($response);

mysqli_close($connDB);
?>
