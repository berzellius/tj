var domReadyInsuranceAreas = (function (pref, msg, imgOnload) {
    return function () {
        var urlPrefix = pref;
        var ajaxMessages = msg;

        $('div#ins_areas a').click(
            function(){
                insuranceAreasWindow.load(this.href);

                return false;
            }
        );



    }
})(urlPrefix, ajaxMessages, "images/ajax_load.gif");

$(document).ready(domReadyInsuranceAreas);

var insuranceAreasWindow = {

    url : null,


    load : function(url){

        $.ajax({
            'url' : url,
            'success' : (function(t){
                return function(d){
                    $.fancybox({
                        'content' : d
                    });
                    t.url = url;
                    t.bindActions(url);
                };
            })(this)

        });
    },

    bindActions : function(url){
        $('div#insuranceObjects form').each(
            (function(t,u){
                return function(){
                    // Перехватываем отправку формы
                    $(this).submit(function(){

                        // Отправляем запрос и перезагружаем окно
                        $.ajax({
                            'url' : this.action,
                            'type' : this.method,
                            'data' : (function(t){
                                var data = {};
                                $('input[type=text], input[type=hidden], select', t).each(function(){
                                    data[this.name] = this.value;
                                });

                                //alert('input[type=text,form=' + $(t).attr('id') + ']');

                               $('input[type=text][form=' + $(t).attr('id') + ']').each(function(){
                                    data[this.name] = this.value;
                               });

                                return data;
                            })(this),
                            'success' : function(){
                                t.reload(url);
                            }
                        });

                        setTimeout(function(){
                            //t.reload(url);
                        }, 100);

                        return false;
                    });
                }
            })(this, url)
        );

        $('a#updateInsObj').click(
            (function(t){
                return function(){
                    t.reload(t.url);
                    return false;
                };
            })(this)
        );
    },

    close : function(){
        $.fancybox.close();
    },

    reload : function(url){
        this.close;
        this.load(url);
    }

}