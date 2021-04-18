<html lang="en">
	<head>
		<meta charset="utf-8">
		<link rel="shortcut icon" type="image/x-icon" href="media/img/favicon.ico">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="Path Of Building Merger">
		<meta name="author" content="KeldonSlayer">
		<title>KeldonSlayer's Site</title>
		<link href="css/bootstrap.css" rel="stylesheet">
		<link href="css/slayer.css" rel="stylesheet">
	</head>
<body>
<h1 style="background-color: #123;text-align: center;font-size: 50px;">Path of Building Merger</h1>
    <?php
        if ($_POST) {
            $pb1 = $_POST['pastebin_1'];
            $pb2 = $_POST['pastebin_2'];
            $pb3 = $_POST['pastebin_3'];
            $pb4 = $_POST['pastebin_4'];
            $pb5 = $_POST['pastebin_5'];
            $pb6 = $_POST['pastebin_6'];
            $pb7 = $_POST['pastebin_7'];
            $pb8 = $_POST['pastebin_8'];
            $pb9 = $_POST['pastebin_9'];
            $pbl = explode("/",$pb1);
            $pb = $pbl[count($pbl) - 1];
            $cnt = count($pbl);
            exec("java -jar ./POBBreaker.jar $pb1 $pb2 $pb3 $pb4 $pb5 $pb6 $pb7 $pb8 $pb9");
            $cont_test1 = file_get_contents("./output/$pb/output.txt");
            $cont_test2 = file_get_contents("./output/$pb/paste.txt");
            echo "<pre>URL = <input id=\"pastelnk\" type=\"text\" value=\"$cont_test1\" style=\"width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;\"> </br>POB = <input id=\"pasteconts\" type=\"text\" value=\"$cont_test2\"style=\"width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;\"/></pre>";
        }
    else
        {
    ?>
    <div class="pbin" style="position:absolute;left:35%;top:20%;width:350px;background-color:#252525;">
        <form action="" method="post">
            <p style="position:relative;display:block;margin:0 auto;color: #CCD;text-align: center;">Put pastebin links in the boxes below</p>
            <input type="text" name="pastebin_1" style="width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;"/>
            <input type="text" name="pastebin_2" style="width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;"/>
            <input type="text" name="pastebin_3" style="width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;"/>
            <input type="text" name="pastebin_4" style="width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;"/>
            <input type="text" name="pastebin_5" style="width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;"/>
            <input type="text" name="pastebin_6" style="width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;"/>
            <input type="text" name="pastebin_7" style="width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;"/>
            <input type="text" name="pastebin_8" style="width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;"/>
            <input type="text" name="pastebin_9" style="width:320px;background-color:#353535;position:relative;display:block;margin:0 auto;color:#fff;"/>
            <input type="submit" value="Merge The Pastes!" style="position:relative;display:block;margin:0 auto;color:#fff;background-color:#303030;"/>
        </form>
    </div>
    <?php
        }
    ?>
</body>
</html>
