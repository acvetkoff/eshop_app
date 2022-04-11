let actualItemPrice = parseFloat($("#actualPrice").text());
// TODO fix jquery to fire event on dynamically added content (thymeleaf generated)

$(".spinner").on("click", ".increment > #incrementQuantity", () => {
    let oldQuantity = parseFloat($("#quantity").val());
    let oldItemTotalPrice = parseFloat($("#orderItemTotalPrice").text());
    let oldOrderTotalPrice = parseFloat($("#orderTotalPrice").text().match(/\d+/));

    $("#quantity").val(oldQuantity + 1);
    $("#orderItemTotalPrice").text((oldItemTotalPrice + actualItemPrice).toFixed(2));
    $("#orderTotalPrice").text("Total: " + (oldOrderTotalPrice + actualItemPrice).toFixed(2));
});

$(".spinner").on("click", ".decrement > #decrementQuantity", () => {

    let oldQuantity = parseFloat($("#quantity").val());
    let oldItemTotalPrice = parseFloat($("#orderItemTotalPrice").text());
    let oldOrderTotalPrice = parseFloat($("#orderTotalPrice").text().match(/\d+/));

    if (oldQuantity <= 1) {
        $("#quantity").val(1)
        $("#orderItemTotalPrice").text(actualItemPrice.toFixed(2));
        $("#orderTotalPrice").text("Total: " + actualItemPrice.toFixed(2));
    } else {
        $("#quantity").val(oldQuantity - 1);
        $("#orderItemTotalPrice").text((oldItemTotalPrice - actualItemPrice).toFixed(2));
        $("#orderTotalPrice").text("Total: " + (oldOrderTotalPrice - actualItemPrice).toFixed(2));
    }
});