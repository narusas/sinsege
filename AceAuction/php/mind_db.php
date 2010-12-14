<?

$host_name = "localhost";
$user_name = "acea";
$db_password ="dhrtus_ace";
$db_name = "test";

$connect=mysql_connect($host_name,$user_name,$db_password);
mysql_select_db($db_name, $connect);

$mind = trim($mind);
$mind = str_replace("/",".",$mind);
$minds = explode("\r\n",$mind);

$filter = array();
$n = 0;

for($n = 0; $n <= count($minds); $n++)
{
        $get = str_replace(" ","",$minds[$n]);
        if(eregi("감정평가법인|감정평가사|감정원",$get))
        {
		if(eregi("한국감정원은",$get))
		{
			$get = "한국감정원";
		}

		echo "감정평가기관 : ".$get."<br>";
		break;
        }
}

for($m = 0; $m <= count($minds); $m++)
{
	$x = 1;
        $set = explode(" ",$minds[$m]);
	for($l = 0; $l <= count($set); $l++)
	{
			
		if(eregi("\.",$set[$l]))
		{	
	                $date_check = explode(".",$set[$l]);
        	        $year = trim($date_check[0]);
                	$month = trim($date_check[1]);
	                $day = trim($date_check[2]);
			if($year > 1997 && $month < 13 && $day < 32)
			{
				echo "감정평가일 : ".$set[$l]."<br><hr><br>";
				$x = 0;
				break;
			}
		}
	}
	if($x == 0) { break; }
}




for($a = 0; $a <= count($minds); $a++)
{
	$get = str_replace(" ","",$minds[$a]);
	if(eregi("요항표",$get))
	{
		$ba = $a + 1;
		for($b = $ba; $b <= count($minds); $b++)
		{
			$filter[$n++] = $minds[$b];
		}
	}
}

$mind_a = array();
$an = 0;

for($c = 0; $c <= count($filter); $c++)
{
	$filter_check = str_replace(" ","",$filter[$c]);
	if(eregi("열람용|요항표|감정평가법인|감정평가사|기호\()",$filter_check))
	{
	}
	elseif(!$filter_check)
	{
	}
	elseif(is_numeric($filter_check))
	{
	}
        elseif(eregi("1\.",$filter_check) && eregi("2\.",$filter_check))
        {
        }
        elseif(eregi("2\.",$filter_check) && eregi("3\.",$filter_check))
        {
        }
        elseif(eregi("3\.",$filter_check) && eregi("4\.",$filter_check))
        {
        }
        elseif(eregi("4\.",$filter_check) && eregi("5\.",$filter_check))
        {
        }
        elseif(eregi("5\.",$filter_check) && eregi("6\.",$filter_check))
        {
        }
        elseif(eregi("6\.",$filter_check) && eregi("7\.",$filter_check))
        {
        }
        elseif(eregi("7\.",$filter_check) && eregi("8\.",$filter_check))
        {
        }
        elseif(eregi("9\.",$filter_check) && eregi("10\.",$filter_check))
        {
        }
        elseif(eregi("10\.",$filter_check) && eregi("11\.",$filter_check))
        {
        }
        elseif(eregi("11\.",$filter_check) && eregi("12\.",$filter_check))
        {
        }
        elseif(eregi("12\.",$filter_check) && eregi("13\.",$filter_check))
        {
        }
        elseif(eregi("\.기타",$filter_check))
        {
        }
        elseif(eregi("1\.",$filter_check) && eregi("4\.",$filter_check))
        {
        }
        elseif(eregi("2\.",$filter_check) && eregi("5\.",$filter_check))
        {
        }
        elseif(eregi("3\.",$filter_check) && eregi("6\.",$filter_check))
        {
        }

	elseif(eregi("위치도|사진용지|내부구조도",$filter_check))
	{
		break;
	}
	else
	{
		$mind_a[$an++] = $filter[$c];
	}
}

$sun1 = array();
$sunno_1 = 0;

$sun2 = array();
$sunno_2 = 0;

$sun3 = array();
$sunno_3 = 0;

$sun4 = array();
$sunno_4 = 0;

$sun5 = array();
$sunno_5 = 0;

for($c = 0; $c <= count($mind_a); $c++)
{
	$check_word = trim(str_replace(" ","",$mind_a[$c]));
	$check = explode(".",$check_word);
	if(is_numeric($check[0]))
	{
		if(eregi("위치및교통|위치및부근의상황|위치교통및주위환경|위치및주위환경|주위환경|교통상황|위치|교통",$check[1]))
		{
			$cn = $c + 1;
			for($cf = $cn; $cf <= count($mind_a); $cf++)
			{
				$check_word2 = trim(str_replace(" ","",$mind_a[$cf]));
				$check2 = explode(".",$check_word2);
				if(is_numeric($check2[0]))
				{
					break;
				}
				else
				{
					$sun1[$sunno_1++] = $mind_a[$cf];
				}
			}
		}


                if(eregi("형상및이용상태|형태및이용상태|토지의상황|형상및이용상황|토지형태및이용상황|형태지세및이용상태|토지의형태및이용상태|형상·지세및이용상태|토지의형상및이용상태|형태|임지상황|토지의형태|토지상황|형태및이용상황|토지의형태및이용상태및인접도로",$check[1]))
                {
                        $cn = $c + 1;
                        for($cf = $cn; $cf <= count($mind_a); $cf++)
                        {
                                $check_word2 = trim(str_replace(" ","",$mind_a[$cf]));
                                $check2 = explode(".",$check_word2);
                                if(is_numeric($check2[0]))
                                {
                                        break;
                                }
                                else
                                {
                                        $sun2[$sunno_2++] = $mind_a[$cf];
                                }
                        }
                }

                if(eregi("토지이용계획관계및제한상태|토지이용계획및기타공법상제한상태|도시계획관계및기타공법상제한상태|도시계획관계및공법상제한상태|토지이용계획관계|토지이용계획및기타공법상관계|도시계획및기타공법상관계|도시계획및기타공법관계|토지이용계획관계및공법상제한상태|도시계획관계및공법상제한의상태|토지이용계획및공법상제한|도시계획및기타공법상의제한상태",$check[1]))
                {
                        $cn = $c + 1;
                        for($cf = $cn; $cf <= count($mind_a); $cf++)
                        {
                                $check_word2 = trim(str_replace(" ","",$mind_a[$cf]));
                                $check2 = explode(".",$check_word2);
                                if(is_numeric($check2[0]))
                                {
                                        break;
                                }
                                else
                                {
                                        $sun3[$sunno_3++] = $mind_a[$cf];
                                }
                        }
                }

                if(eregi("인접도로상태|도로상태|도로상태및주차장시설등",$check[1]))
                {
                        $cn = $c + 1;
                        for($cf = $cn; $cf <= count($mind_a); $cf++)
                        {
                                $check_word2 = trim(str_replace(" ","",$mind_a[$cf]));
                                $check2 = explode(".",$check_word2);
                                if(is_numeric($check2[0]))
                                {
                                        break;
                                }
                                else
                                {
                                        $sun4[$sunno_4++] = $mind_a[$cf];
                                }
                        }
                }

                if(eregi("냉난방설비|위생및냉난방설비|위생냉난방시설등|위생설비및냉난방설비등|냉난방설비등주요설비및기타설비|위생냉난방설비및기타설비|냉난방설비등",$check[1]))
                {
                        $cn = $c + 1;
                        for($cf = $cn; $cf <= count($mind_a); $cf++)
                        {
                                $check_word2 = trim(str_replace(" ","",$mind_a[$cf]));
                                $check2 = explode(".",$check_word2);
                                if(is_numeric($check2[0]))
                                {
                                        break;
                                }
                                else
                                {
                                        $sun5[$sunno_5++] = $mind_a[$cf];
                                }
                        }
                }


	}

}


$locationComment  = "";
$useComment  = "";
$landComment  = "";
$roadComment  = "";
$tempComment  = "";

echo "1. 위치,교통관련 : <br>";
for($zz = 0; $zz <= count($sun1); $zz++)
{
	echo $sun1[$zz];
	$locationComment .= $sun1[$zz];
}
//$locationComment = addslashes($locationComment);
echo "<br><hr>";

echo "2. 이용상태관련 : <br>";
for($zz = 0; $zz <= count($sun2); $zz++)
{
        echo $sun2[$zz];
        $useComment .= $sun2[$zz];
}
//$useComment = addslashes($useComment);
echo "<br><hr>";

echo "3. 토지관련 : <br>";
for($zz = 0; $zz <= count($sun3); $zz++)
{
        echo $sun3[$zz];
        $landComment  .= $sun3[$zz];
}
//$landComment = addslashes($landComment);
echo "<br><hr>";

echo "4. 도로관련 : <br>";
for($zz = 0; $zz <= count($sun4); $zz++)
{
        echo $sun4[$zz];
        $roadComment .= $sun4[$zz];
}
//$roadComment = addslashes($roadComment);
echo "<br><hr>";

echo "5. 냉난방 관련 : <br>";
for($zz = 0; $zz <= count($sun5); $zz++)
{
        echo $sun5[$zz];
        $tempComment .= $sun5[$zz];
}

//$tempComment = addslashes($tempComment);

echo "<br><br>";
$query = "UPDATE ac_event SET judgement_location='$locationComment', judgement_use='$useComment', judgement_land='$landComment', judgement_road='$roadComment', judgement_temp='$tempComment' WHERE id='$sagunId';";

$rs = mysql_query($query, $connect);
echo "OK: $sagunId";
?>


