<?php

$nome=$_POST['nome'];
$cognome=$_POST['cognome'];
$localita=$_POST['localita'];
$username=$_POST['username'];
$password=$_POST['password'];
$query="INSERT INTO users(nome, cognome, localita, username, password)
VALUES ('$nome', '$cognome', '$localita','$username','$password')";
$db=new mysqli("localhost", "mtd", "", "my_mtd");

if ($db->connect_errno) {
    echo "Failed to connect to MySQL: ".$db->connect_error;
    exit();
}

if (! $result=$db->query($query)) {
   echo "Nessun risultato";
   }
   else 
   echo "FATALE";
   
?>