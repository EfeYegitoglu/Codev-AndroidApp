<?php

$response = array();

if(isset($_POST['user_id'])){
    $userId = $_POST['user_id'];

    require_once __DIR__ . '/db_config.php';

    $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!$baglanti){die("Hatalı bağlantı : " . mysqli_connect_error());}

    $control = mysqli_query($baglanti,"SELECT * FROM posts WHERE user_id = '$userId'");
    $count = mysqli_num_rows($control);

    if($count > 0){

            $response["success"] = 1;
            $response["message"] = "$count";
            echo json_encode($response);
            mysqli_close($baglanti);



    }else{

        $response["success"] = 2;
        $response["message"] = "0";
        echo json_encode($response);
        mysqli_close($baglanti);
    }



}else{
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}


?>
