if (document.forms['order_form']) {
    updateOrderTotal();
}

function updateOrderTotal() {
    if (document.getElementById("amount-due")) {
        document.getElementById("amount-due").innerHTML = '$' + getOrderTotal();
    }
}

function getOrderTotal() {
    let total = 0;
    const item_quantities = document.forms['order_form'].getElementsByClassName("item-quantity-input");
    for (const item of item_quantities) {
        const unit_price = parseFloat(item.getAttribute("price"));
        const unit_quantity = parseInt(item.value);
        total += unit_price * unit_quantity;
    }
    return total.toFixed(2);
}

function submitOrder() {
    document.forms['order_form'].submit();
}

//timeout
setTimeout(function(){
    window.location.href = '/timeout';
}, 60000);