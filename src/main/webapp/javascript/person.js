   var domReady = (function(pref, msg){
    return function(){
        var urlPrefix = pref;
        var ajaxMessages = msg;

        $("#fill_person").click(
            function(){
                $.get(
                    urlPrefix+"persons?form",
                    function(d){
                        $.fancybox(d);
                        bindPersonActions(urlPrefix, ajaxMessages);
                        filters();
                        $('div.date input[type=text]').datepicker({
                            dateFormat: 'yy-mm-dd'
                        });
                    }
                );
                return false;
            }
        );

        var bindPersonActions = function(urlPrefix, ajaxMessages){

            var urlPrefix = urlPrefix;
            var ajaxMessages = ajaxMessages;

            $("input#proceed").attr('disabled', true);

            $("a#addr_equals").click(function(){

                $("input[name=addrResident]").val(
                    $("input[name=addrRegistr]").val()
                );
                $("input[name=indexResident]").val(
                    $("input[name=indexRegistr]").val()
                );

                return false;
            });

            $("input#check_btn").click(function(){
                var formData = {};
                $("form#person input[type=text]").each(function(){
                    if(this.name) formData[this.name] = this.value;
                });
                $.post(
                    urlPrefix+"persons?search",
                    formData,
                    function(d){
                        var res = JSON.parse(d);
                        $("input#proceed").attr('disabled', false);
                        if(res.length > 0){
                            $("#foundPeople").css("display","block");
                            $("#createNewPerson").css("display","none");
                            $("#foundPeople").append("<form id='foundPeopleForm'><table></table></form>");
                            for(p in res){
                                obj = res[p];

                                $("form#foundPeopleForm table").append("<tr><td><!--input type='radio' name='Person' value='"+
                                    obj.id +"' /--></td><td><a href='' id='person" + obj.id + "'>" + obj.info + "</a></td></tr>");

                                $("#person"+obj.id).click(
                                    (function(o){
                                        return function(){
                                            $("input[name=person]").val(o.id);
                                            $("p#personValue").html(o.info);
                                            $.fancybox.close();
                                            return false;
                                        }
                                    })(obj)
                                );
                            }
                        }
                        else{
                            $("#foundPeople").css("display","none");
                            $("#createNewPerson").css("display","block");
                        }
                    }
                );
                return false;
            });

            /*$("a#newPerson").click(function(){
                var formData = {};
                $("form#person input[type=text]").each(function(){
                    if(this.name) formData[this.name] = this.value;
                });

                $.post(
                    urlPrefix+"persons?new",
                    formData,
                    function(d){
                        var res = JSON.parse(d);
                        if(res.success){
                            var p = res.personAjax;
                            $("input[name=person]").val(p.id);
                            $("p#personValue").html(p.info);
                            $.fancybox.close();
                        }
                        else{
                            $("#createNewPerson").append("<p style='color:red'><b>"+ ajaxMessages.SM_HAPPEN +":</b> " + res.message + " </p>");
                        }
                    }
                );

                return false;
            });*/

            $("form#person").submit(function(){


               var formData = {};

               $("form#person input[type=text]").each(function () {
                   if (this.value == '') $("#createNewPerson").html("<p style='color:red'><b>" + ajaxMessages.FILL_ALL + "</b></p>");

                   if (this.name) formData[this.name] = this.value;
               });


                $.post(
                    urlPrefix+"persons?new",
                    formData,
                    function(d){
                        var res = JSON.parse(d);
                        if(res.success){
                            var p = res.personAjax;
                            $("input[name=person]").val(p.id);
                            $("p#personValue").html(p.info);
                            $.fancybox.close();
                        }
                        else{
                            $("#createNewPerson").append("<p style='color:red'><b>"+ ajaxMessages.SM_HAPPEN +":</b> " + res.message + " </p>");
                        }
                    }
                );

               return false;
            });
        };

    }
})(urlPrefix, ajaxMessages);

$(document).ready(domReady);