<?
// print_r ($_POST);
// print_r($_FILES);

if ($_FILES != NULL) {
	// Where the file is going to be placed 
	$temp = "data/";
	if (! file_exists($temp) ){
		mkdir($temp);
	}
		
	$target_path = $temp .$_POST["path"];
	echo $target_path ."\n";
	$tokens = split("/",$target_path);
	$dir = "";
	foreach($tokens as $token) {
		$dir = $dir.$token."/";		
		echo $dir."\n";
		if (! file_exists($dir) ){
			mkdir($dir);
		}
	
	}
	$target_path = $target_path .$_POST["filename"];
	
	$isSuccess = move_uploaded_file($_FILES['UPLOADFILE']['tmp_name'], $target_path);
	
}
?>