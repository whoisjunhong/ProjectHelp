<?php
# FileName="Connection_php_mysql.htm"
# Type="MYSQL"
# HTTP="true"
$hostname_connDB = "localhost"; //assume database is in the same server

$database_connDB = "projecthelpdb";
$username_connDB = "root";
$password_connDB = "";

$connDB = mysqli_connect($hostname_connDB, $username_connDB, $password_connDB, $database_connDB) or trigger_error(mysqli_error(),E_USER_ERROR); 
?>