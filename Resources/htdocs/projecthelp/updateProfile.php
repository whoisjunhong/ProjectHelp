<?php require_once('Connections/connDB.php'); ?>
<?php
$response = array();

$input = file_get_contents("php://input");
$jsonObj = json_decode($input, true);
$id = $jsonObj['id'];
$username = $jsonObj['username'];
$password = $jsonObj['password'];
$unitno = $jsonObj['unitno'];
$address = $jsonObj['address'];
$status = $jsonObj['msgType'];

if ($status == "update") {
    $query_rsUser = sprintf("UPDATE profile SET username = '%s', password = '%s', address = '%s', unitno = '%s' WHERE id = %s", $username, $password, $address, $unitno, $id);
    $rsUser = mysqli_query($connDB, $query_rsUser);
    $response["id"] = $id;
  	$response["username"] = $username;
  	$response["password"] = $password;
    $response["address"] = $address;
    $response["unitno"] = $unitno;

  if ($rsUser != 0)
	{
		$response["updateStatus"] = "UpdateSuccess";
	}
	else
	{
		$response["updateStatus"] = "UpdateFailed";
	}
}
else  { // get
    $query_rsUser = sprintf("SELECT * FROM profile WHERE id = %s", $id);

    $rsUser = mysqli_query($connDB, $query_rsUser);
    $row_rsUser = mysqli_fetch_assoc($rsUser);

    $response["id"] = $id;
    $response["username"] = $row_rsUser['username'];
    $response["name"] = $row_rsUser['name'];
    $response["password"] = $row_rsUser['password'];
    $response["usertype"] = $row_rsUser['usertype'];
    $response["gender"] = $row_rsUser['gender'];
    $response["address"] = $row_rsUser['address'];
    $response["unitno"] = $row_rsUser['unitno'];

    if (mysqli_num_rows($rsUser) == 1 ) {
        $response["status"] = "SelectSuccess";
    }
    else {
        $response["status"] = "SelectFailed";
    }
}

$response["msgType"] = $status;

echo json_encode($response);

mysqli_close($connDB);
?>
