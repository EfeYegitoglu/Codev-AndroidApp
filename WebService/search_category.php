<?php
    
    $response = array();
    
    if (isset($_POST['category_name'])) {
        $category = $_POST['category_name'];
        
        //DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE değişkenleri alınır.
        require_once __DIR__ . '/db_config.php';
        
        // Bağlantı oluşturuluyor.
        $baglanti = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
        
        // Bağlanti kontrolü yapılır.
        if (!$baglanti) {
            die("Hatalı bağlantı : " . mysqli_connect_error());
        }
        
        $sqlsorgu = "SELECT * FROM category WHERE category_name like '%$category%'";
        $result = mysqli_query($baglanti, $sqlsorgu);
        
        // result kontrolü yap
        if (mysqli_num_rows($result) > 0) {
            
            $response["categories"] = array();
            
            while ($row = mysqli_fetch_assoc($result)) {
                // temp user array
                $categories = array();
                $categories["category_id"] = $row["category_id"];
                $categories["category_name"] = $row["category_name"];
                $categories["category_picture"] = $row["category_picture"];
                $categories["category_post_count"] = $row["category_post_count"];
                
                array_push($response["categories"], $categories);
            }
            // success
            $response["success"] = 1;
            
            // echoing JSON response
            echo json_encode($response);
            
        } else {
            // no products found
            $response["success"] = 0;
            $response["message"] = "No data found";
            
            // echo no users JSON
            echo json_encode($response);
        }
        
        //bağlantı koparılır.
        mysqli_close($baglanti);
        
        
    } else {
        
        $response["success"] = 0;
        $response["message"] = "Required field(s) is missing";
        
        echo json_encode($response);
    }
    ?>