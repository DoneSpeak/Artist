$(function(){
    $(".publishers-btn").on("click",function(){
        // $(".publishers").toggle(500);
        var display = $(".publishers").css("display");
        if(display == "none"){
            $(".publishers").show();
        }else{
            $(".publishers").hide();
        }
    });

    $(".search-type-item").on("click",function(){
        var data = $(this).data();
        $(".search-type-text").text(data.text);

        $("#type").val(data.value);
    });

    $("#search-type-btn").on("click",function () {
        $(".publishers").hide();
    });

    $(".option-duration-item").on("click",function(){
        var data = $(this).data();
        $(".option-duration-text").text(data.text);
        $(".option-duration-text").addClass("cur-option");

        $("#duration").val(data.value);
    });

    $(".option-klass-item").on("click",function(){
        var data = $(this).data();
        $(".option-klass-text").text(data.text);
        $(".option-klass-text").addClass("cur-option");

        $("#klass").val(data.value);
    });

    $(".option-publisher-item").on("click",function(){
        var data = $(this).data();
        $(".option-publisher-text").text(data.text);
        $(".option-publisher-text").addClass("cur-option");
        $(".publishers").hide();

        $("#publisher").val(data.value);
    });

    $("#type-dropdown-menu").on("click",function(){
        $(".publishers").hide();
    });

    $("#duration-dropdown-menu").on("click",function(){
        $(".publishers").hide();
    });

})