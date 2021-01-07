<?php

$response = array();

if(isset($_POST['post_id']) && isset($_POST['user_id'])){

    $postId = $_POST['post_id'];
    $userId = $_POST['user_id'];


    require_once __DIR__ . '/db_config.php';

    $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!$baglanti){
    die ("Hatalı bağlantı: " . mysqli_connect_error());
    }

    $sqlsorgu = "INSERT INTO stars (post_id,user_id) VALUES ('$postId','$userId')";

    if(mysqli_query($baglanti, $sqlsorgu)){
        $response["success"] = 1;
        $response["message"] = "successfully ";
        echo json_encode($response);
    }else{
        $response["success"] = 2;
        $response["message"] = "No product found";
        echo json_encode($response);
    }
    mysqli_close($baglanti);

}else{
    $response["success"] = 3;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}

?>
