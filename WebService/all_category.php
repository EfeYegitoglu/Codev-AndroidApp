<?php
// array for JSON response
$response = array();
    
//DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE değişkenleri alınır.
require_once __DIR__ . '/db_config.php';

// Bağlantı oluşturuluyor.
$conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

// Bağlanti kontrolü yapılır.
if (!$conn) {
    die("Hatalı bağlantı : " . mysqli_connect_error());
}

$sqlquery = "SELECT * FROM category";
$result = mysqli_query($conn, $sqlquery);

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
mysqli_close($conn);
?>