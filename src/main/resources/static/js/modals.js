$('.cart-item').each(() => {
    $('.trash-can').on('click', (e) => {
        e.preventDefault();
        console.log(e.target);
        $('.delete-modal').show();
    })

    $('.cancel').on('click', (e) => {
        e.preventDefault();
        $('.delete-modal').hide();
    });
});

$('#checkOutBtn').on('load', (e) => {
   e.preventDefault();
   $('#modal').show();

   $('#closeBtn').on('click', (e) => {
       e.preventDefault();
       $('#modal').hide();
   });
});