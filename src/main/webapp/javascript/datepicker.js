/**
 * Created by berz on 30.03.14.
 */
setTimeout(
    function(){
        $('div.date input[type=text]').datepicker({
            dateFormat: 'dd.mm.yy'
        });
    },1000
);