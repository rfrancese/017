<?php

$nome=$_GET['nome'];
$cognome=$_GET['cognome'];
$query="SELECT username FROM users WHERE nome='$nome' and cognome='$cognome'";

$db=new mysqli("localhost", "mtd", "", "my_mtd");

if ($db->connect_errno) {
    echo "Failed to connect to MySQL: ".$db->connect_error;
    exit();
}

if ($result=$db->query($query)) {
    $arr= array();
	
    while ($row=mysqli_fetch_assoc($result)) {
        $arr[]=$row;
    }
    $data=array('compagniViaggio'=>$arr);
    echo json_encode($data);

    //echo json_encode($result->fetch_assoc());
}

if (mysqli_num_rows($result)==0) {
	echo "Nessun risultato";
}
?>