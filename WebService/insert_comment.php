<?php

$response = array();

if(isset($_POST['user_id']) && isset($_POST['comment_text'])  && isset($_POST['post_id'])){

    $commentText = $_POST['comment_text'];
    $userId = $_POST['user_id'];
    $postId = $_POST['post_id'];


    require_once __DIR__ . '/db_config.php';

    $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!$baglanti){
    die ("Hatalı bağlantı: " . mysqli_connect_error());
    }

    $sqlsorgu = "INSERT INTO comments (post_id,comment_text,user_id) VALUES ('$postId','$commentText','$userId')";

    if(mysqli_query($baglanti, $sqlsorgu)){
        $response["success"] = 1;
        $response["message"] = "Paylaşım Başarılı";
        echo json_encode($response);
    }else{
        $response["success"] = 2;
        $response["message"] = "Başarısız";
        echo json_encode($response);
    }
    mysqli_close($baglanti);

}else{
    $response["success"] = 0;
    $response["message"] = "Bağlantı Hatası";
    echo json_encode($response);
}

?>
