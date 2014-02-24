<?php

// Receives all data about user and sends all locations to app
 
$hr = date('H');

// log data about user interaction to database
if( isset($_POST['time']) && isset($_POST['log']) && isset($_POST['id']) && isset($_POST['curLat']) && isset($_POST['curLng']) ) {

	$user_id = $_POST['id'];
	$category = $_POST['log'];
	$time = $_POST['time'];
	
	//This will be a post request
	$url = "https://testtttt.azure-mobile.net/api/post?userid=".$user_id."&category=".$category."&hour=".$hr."&time_spent=".$time;
	$file= file_get_contents($url);
	//$userJson = json_decode($file);

// send out locations based on user data	
} else if( isset($_POST['id']) && isset($_POST['curLat']) && isset($_POST['curLng']) ) {

	$user_id = $_POST['id'];
	$curLat = $_POST['curLat'];
	$curLong = $_POST['curLng'];
	
	//This will be a get request
	$url = "https://testtttt.azure-mobile.net/api/under?userid=".$user_id;
	$file= file_get_contents($url);
	//echo $file;
	$userJson = json_decode($file);
	//echo $userJson;
	//$assoc = new Array();
	$noUserJsonFlag = false;
	if ($userJson == "None"){
		$noUserJsonFlag = true;
	}
	// check preferences
	else {
		$numRows = count($userJson);
		for ($x=0; $x<$numRows; $x++)
		{
			if(($userJson[$x]->hour >= $hr-2) && ($userJson[$x]->hour <= $hr+2))
			{
				if($assoc[$userJson[$x]->category])
					$assoc[$userJson[$x]->category] = $assoc[$userJson[$x]->category] + $userJson[$x]->time_spent;
				else
					$assoc[$userJson[$x]->category] = $userJson[$x]->time_spent;
					
			}
		}
	
		arsort($assoc);
		$preference = array();
		foreach($assoc as $i=>$i_value)
		{
			array_push($preference, $i);
		}
	}
	// trending locations from Foursquare
	$url = "https://api.foursquare.com/v2/venues/trending?ll=".$curLat.",".$curLong."&oauth_token=MDH0BXNSAQAFY1UG0YLDJEPT0NYF5CE21JA1GMDGN15IN0XE&v=20140125";
	//$url = 'https://api.foursquare.com/v2/venues/trending?ll=-33.8600,151.2111&oauth_token=MDH0BXNSAQAFY1UG0YLDJEPT0NYF5CE21JA1GMDGN15IN0XE&v=20140125';
	$file= file_get_contents($url);
	 
	$json = json_decode($file);
	$count = count($json->{'response'}->{'venues'});
	
	if($count == 0) {
		$vurl = "https://api.foursquare.com/v2/venues/search?ll=".$curLat.",".$curLong."&oauth_token=MDH0BXNSAQAFY1UG0YLDJEPT0NYF5CE21JA1GMDGN15IN0XE&v=20140125";
		$vfile= file_get_contents($vurl);
	 
		$vjson = json_decode($vfile);
		
		//$vjsonstring = json_encode($vjson);
		
		if($noUserJsonFlag == true) {
			echo json_encode($vjson->{'response'}->{'venues'});
			die();
		}
		$vcount = count($vjson->{'response'}->{'venues'});
		///////////////////////////////////////
		$vrecord = array();

		for ($x=0; $x<$vcount; $x++)
		{

			//$jsonstring = json_encode($json->{'response'}->{'venues'}[$x]->{'categories'}[0]->name);
			$vrecord[$x] =  $vjson->{'response'}->{'venues'}[$x]->{'categories'}[0]->name;
			//echo $record[$x];
		} 

			

		$prefCount = count($preference);



		$newList = array();

		// check preferences
		for ($x=0; $x<$prefCount; $x++)
		{
			for ($y=0; $y<$vcount; $y++)
			{
				if($vrecord[$y] == $preference[$x])
				{
					array_push($newList, $vjson->{'response'}->{'venues'}[$y]); 
					$vrecord[$y] = null;
					
				}
			}
		} 

		for ($y=0; $y<$vcount; $y++)
			{
				if($vrecord[$y] != null)
				{
					array_push($newList, $vjson->{'response'}->{'venues'}[$y]); 
					$vrecord[$y] = null;
					
				}
			}
	

		$vjsonstring = json_encode($newList);
				echo $vjsonstring;
		
		//////////////////////////////////////
		
		die();
	}

	
	
	/////////////////////////////////////
	if($noUserJsonFlag == true) {
		echo $json->{'response'}->{'venues'};
		die();
	}
	
	$record = array();

	// format locations for pushing
	for ($x=0; $x<$count; $x++)
  	{

  		//$jsonstring = json_encode($json->{'response'}->{'venues'}[$x]->{'categories'}[0]->name);
  		$record[$x] =  $json->{'response'}->{'venues'}[$x]->{'categories'}[0]->name;
		//echo $record[$x];
  	} 

  		

  	$prefCount = count($preference);



  	$newList = array();

	// final format before push
  	for ($x=0; $x<$prefCount; $x++)
  	{
  		for ($y=0; $y<$count; $y++)
  		{
  			if($record[$y] == $preference[$x])
  			{
  				array_push($newList, $json->{'response'}->{'venues'}[$y]); 
  				$record[$y] = null;
  				
  			}
  		}
  	} 

  	for ($y=0; $y<$count; $y++)
  		{
  			if($record[$y] != null)
  			{
  				array_push($newList, $json->{'response'}->{'venues'}[$y]); 
  				$record[$y] = null;
  				
  			}
  		}

	

	$jsonstring = json_encode($newList);
				echo $jsonstring;
	///////////////////////////////////

} else {
	echo "ERROR";
}
?>