<?php require_once ("Connections/connDB.php") ?>
<?php
$response = array();

$input = file_get_contents("php://input");
$jsonObj = json_decode($input, true);
$requestType = $jsonObj['msgType'];
$aceptee = $jsonObj['aceptee'];
$requestId = $jsonObj['requestId'];

$query_rsApply = sprintf("INSERT INTO scheduled (aceptee, requestId) VALUES ('%s', '%s')", $aceptee, $requestId);
$quert_rsApplyUpdate = sprintf("UPDATE request SET status = 'P' WHERE requestId = %s", $requestId);
$rsInsertApplication = mysqli_query($connDB, $query_rsApply);
$rsInsertApplication1 = mysqli_query($connDB, $quert_rsApplyUpdate);
$rsID = mysqli_insert_id($connDB);

if ($rsInsertApplication != 0 && $rsInsertApplication1 != 0)
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
