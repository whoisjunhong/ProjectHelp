<?php require_once ("Connections/connDB.php") ?>
<?php
$response = array();

$input = file_get_contents("php://input");
$jsonObj = json_decode($input, true);
$requestType = $jsonObj['msgType'];
$requestId = $jsonObj['requestId'];

$query_rsDelete = sprintf("DELETE FROM request WHERE requestId = %s", $requestId);
$rsDeleteRequest = mysqli_query($connDB, $query_rsDelete);

if ($rsDeleteRequest != 0)
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
