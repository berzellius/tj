/**
 * Created by berz on 03.04.14.
 */
var contractLifeCycle = (function(msg, pref){
    return function(){
        var ajaxMessages = msg;
        var urlPrefix = pref;

        $('input[type=button]#contractSave').click(function(){
            $('form#contract').submit();
        });

        $('input[type=button]#contractPrint').click(function(){
            var id = $('form#contract input[name=id]').val();

            $.get(
                urlPrefix + "contracts?print",
                {'id' : id},
                function(d){
                    var res = JSON.parse(d);
                    if(res.success){
                        var win = window.open(urlPrefix + "contracts/"+id+"?print=yes");
                        window.location.href = urlPrefix + "contracts?page=1";
                    }
                    else alert(res.message);
                }
            );
        });

        $('input[type=button]#contractClose').click(function(){
            window.location.href= urlPrefix + "contracts?page=1";
        });
    }
})(ajaxMessages, urlPrefix);

$(document).ready(contractLifeCycle);