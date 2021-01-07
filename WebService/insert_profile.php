<?php

$response = array();

if(isset($_POST['profile_name']) && isset($_POST['profile_info']) && isset($_POST['profile_website']) && isset($_POST['user']) ){

    $profile_picture = rand(0,100000).rand(0,100000).rand(0,100000).rand(0,100000);
    $profile_picture_url = "/storage/ssd3/659/14660659/public_html/coderapp/profileimages/$profile_picture.jpg";
    $profile_name = $_POST['profile_name'];
    $profile_info = $_POST['profile_info'];
    $profile_website = $_POST['profile_website'];
    $user = $_POST['user'];
    $profilePricture = $_POST['profile_picture'];
    if(empty($profilePricture)){
        $profile_picture = "";
        $profile_picture_url = "";
    }

    require_once __DIR__ . '/db_config.php';

    $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!$baglanti){
    die ("Hatalı bağlantı: " . mysqli_connect_error());
    }

    $sqlsorgu = "INSERT INTO profiles (profile_picture,profile_picture_url,profile_name,profile_info,profile_website,user) VALUES ('$profile_picture','$profile_picture_url','$profile_name','$profile_info','$profile_website','$user')";

    if(mysqli_query($baglanti, $sqlsorgu)){
        
         if(!empty($profilePricture)){
            file_put_contents($profile_picture_url,base64_decode($profilePricture));
        }
        
        $response["success"] = 1;
        $response["message"] = "successfully ";
        echo json_encode($response);
    }else{
        $response["success"] = 0;
        $response["message"] = "No product found";
        echo json_encode($response);
    }
    mysqli_close($baglanti);

}else{
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}

?>