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
$name = $jsonObj['name'];
$password = $jsonObj['password'];
$gender = $jsonObj['gender'];
$address = $jsonObj['address'];
$unitno = $jsonObj['unitno'];
$userType = $jsonObj['usertype'];
$requestType = $jsonObj['type'];
$checkLogin = generateRandomString();


	$query_rsInsertProfile = sprintf("INSERT INTO profile (username, name, password, gender, address, unitno, usertype, checklogin) VALUES ('%s', '%s', '%s', '%s', '%s','%s', '%s', '%s')", $username, $name, $password, $gender, $address, $unitno, $userType, $checkLogin);
	$rsInsertProfile = mysqli_query($connDB, $query_rsInsertProfile);
	$rsID = mysqli_insert_id($connDB);

	$response["type"] = $requestType;
	$response["id"] = $rsID;
	$response["username"] = $username;
	$response["name"] = $name;
	$response["password"] = $password;
	$response["gender"] = $gender;
	$response["address"] = $address;
	$response["unitno"] = $unitno;
	$response["usertype"] = $userType;

	if ($rsInsertProfile != 0)
	{
			$response["checkLogin"] = $checkLogin;
			$response["status"] = "OK";
	}
	else
	{
		$response["status"] = "NOK";
	}

echo json_encode($response);

mysqli_close($connDB);
?>
