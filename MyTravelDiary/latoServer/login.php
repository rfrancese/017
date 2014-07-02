<?php

header('Content-Type: application/json');
$username=$_POST['username'];
$password=$_POST['password'];
$query="SELECT username, nome, cognome, localita FROM users WHERE username='$username' AND password='$password'";
$db=new mysqli("localhost", "mtd", "", "my_mtd");

if ($db->connect_errno) {
    echo "Failed to connect to MySQL: ".$db->connect_error;
    exit();
}

if ($result=$db->query($query)) {

	while ($row=mysqli_fetch_assoc($result)) {
    	echo json_encode($row);
    }
    
    //echo json_encode($result->fetch_assoc());
}

if (mysqli_num_rows($result)==0) {
	echo "Nessun risultato";
}
?>