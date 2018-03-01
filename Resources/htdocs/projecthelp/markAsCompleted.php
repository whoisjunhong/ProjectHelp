<?php require_once ("Connections/connDB.php") ?>
<?php
$response = array();

$input = file_get_contents("php://input");
$jsonObj = json_decode($input, true);
$requestType = $jsonObj['msgType'];
$requestId = $jsonObj['requestId'];

$query_rsCompleted = sprintf("UPDATE request SET status = 'F' WHERE requestId = %s", $requestId);
$rsInsertApplication = mysqli_query($connDB, $query_rsCompleted);
$rsID = mysqli_insert_id($connDB);

if ($rsInsertApplication != 0)
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
