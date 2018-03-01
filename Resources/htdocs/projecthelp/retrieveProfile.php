<?php require_once('Connections/connDB.php'); ?>
<?php
$response = array();

function generateRandomString() {
	$length = 10;
	$characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	$charactersLength = strlen($characters);
	$randomString = '';
	for ($i = 0; $i < $length; $i++) {
			$randomString .= $characters[rand(0, $charactersLength - 1)];
	}
	return $randomString;
}

$input = file_get_contents("php://input");
$jsonObj = json_decode($input, true);
$username = $jsonObj['username'];
$password = $jsonObj['password'];
$requestType = $jsonObj['type'];
$checkLogin = generateRandomString();

$query_rsUser = sprintf("SELECT * FROM profile WHERE username = '%s' AND password = '%s'", $username, $password);
$rsUser = mysqli_query($connDB, $query_rsUser);

$response["type"] = $requestType;

if(mysqli_num_rows($rsUser) > 0){
	$row_rsUser = mysqli_fetch_assoc($rsUser);
	$response["id"] = $row_rsUser["id"];
	$id = $row_rsUser["id"];
	$response["usertype"] = $row_rsUser["usertype"];
	$checkLogin = generateRandomString();
	$query_addVerifyer = sprintf("UPDATE profile SET checklogin = '%s' WHERE id = %s", $checkLogin, $id);
	$addVerifyer = mysqli_query($connDB, $query_addVerifyer);

	if ($addVerifyer != 0) {
		$response["checkLogin"] = $checkLogin;
		$response['name'] = $row_rsUser["name"];
		$response['username'] = $row_rsUser["username"];
		$response['usertype'] = $row_rsUser["usertype"];
		$response["status"] = "OK";
	}
	else {
		$response["status"] = "NOK";
	}
}
else {
	$response["id"] = "";
	$response["usertype"] = "";
	$response["status"] = "NOK";
}

echo json_encode($response);

mysqli_close($connDB);
?>
