<?php

$response = array();


    $user_id = $_POST['user_id'];
    $post_picture_one = rand(0,100000).rand(0,100000).rand(0,100000).rand(0,100000);
    $post_picture_two = rand(0,100000).rand(0,100000).rand(0,100000).rand(0,100000);
    $post_picture_three = rand(0,100000).rand(0,100000).rand(0,100000).rand(0,100000);
    $post_picture_url_one = "/storage/ssd3/659/14660659/public_html/coderapp/postpictures/$post_picture_one.jpg";
    $post_picture_url_two = "/storage/ssd3/659/14660659/public_html/coderapp/postpictures/$post_picture_two.jpg";
    $post_picture_url_three = "/storage/ssd3/659/14660659/public_html/coderapp/postpictures/$post_picture_three.jpg";
    $post_title = $_POST['post_title'];
    $post_explanation = $_POST['post_explanation'];
    $post_category = $_POST['post_category'];
    $picture_one=$_POST['picture_one'];
    $picture_two=$_POST['picture_two'];
    $picture_three=$_POST['picture_three'];
    
    if(empty($picture_one)){
        $post_picture_one="";
        $post_picture_url_one="";
    }
    if(empty($picture_two)){
        $post_picture_two="";
        $post_picture_url_two="";
    }
    if(empty($picture_three)){
        $post_picture_three="";
        $post_picture_url_three="";
    }
    
    

    require_once __DIR__ . '/db_config.php';

    $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!$baglanti){
    die ("Hatalı bağlantı: " . mysqli_connect_error());
    }

    $sqlsorgu = "INSERT INTO posts (user_id,post_picture_one,post_picture_url_one,post_picture_two,post_picture_url_two,post_picture_three,post_picture_url_three,post_title,post_explanation,post_category) VALUES ('$user_id','$post_picture_one','$post_picture_url_one','$post_picture_two','$post_picture_url_two','$post_picture_three','$post_picture_url_three','$post_title','$post_explanation','$post_category')";

    if(mysqli_query($baglanti, $sqlsorgu)){
        
        if(!empty($picture_one)){
            file_put_contents($post_picture_url_one,base64_decode($picture_one));
        }
        
        if(!empty($picture_two)){
            file_put_contents($post_picture_url_two,base64_decode($picture_two));
        }
        
        if(!empty($picture_three)){
            file_put_contents($post_picture_url_three,base64_decode($picture_three));
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



?>