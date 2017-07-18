<?php
	header( 'content-type: text/html; charset=utf-8' );
    $collegamento = 'mysql:host=localhost;dbname=my_onlinemuseum';
	try {
		$dbConn = new PDO($collegamento , 'onlinemuseum', '');
	}catch(PDOException $e) {
		echo 'Impossibile connettersi al database!';
	}
	$dbConn->exec('set names utf8');
	$output='';
	$risultato = $dbConn->prepare('SELECT codice_opera, nome_opera, descrizione, luogo, autore, periodo_storico, tecnica, dimensioni FROM elenco_opere;');
	if(!(isset($risultato))){
		echo 'Impossibile eseguire la query!';
		break;
	}
	$risultato->execute();
	$temp_array = array();
	while($row = $risultato->fetch(PDO::FETCH_ASSOC)) {
		$array = array();
		$array[] = array('codice_opera' => $row['codice_opera'], 'nome_opera' => $row['nome_opera'] , 'descrizione' => $row['descrizione'], 'luogo' => $row['luogo'], 'autore' => $row['autore'], 'periodo_storico' => $row['periodo_storico'], 'tecnica' => $row['tecnica'], 'dimensioni' => $row['dimensioni']);
		$temp_array[] = $array;
	}
	
	header('Content-Type: application/json');
	echo json_encode(array('opere'=>$temp_array));
?>
