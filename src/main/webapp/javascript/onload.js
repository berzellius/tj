/**
 * Created by berz on 30.03.14.
 */
var filters = function(){
    $('input.digits').filter_input({regex:'[0-9]'});
    $('input.email').filter_input({regex:'[A-Za-z0-9_\.\-@]'});
}
$(document).ready(function(){
    filters();
});
