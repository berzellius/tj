/**
 * Created by berz on 07.04.14.
 */
var searchContract = (function(msg,pref){

    return function(){
        var ajaxMessages = msg;
        var urlPrefix = pref;

        $('a.keySort').click(function(){
            data = $(this).attr('href').split("|");
            $("form#searchCond input[name=orderColumn]").val(data[0]);
            $("form#searchCond input[name=orderType]").val(data[1]);

            $('tr#searchTr input[type=submit]').click();

            return false;
        });

        $('tr#searchTr input#reset').click(function(){
            $('tr#searchTr input[type=text],form#searchCond input[type=hidden], tr#searchTr select').each(function(){
                $(this).val('');
            });

            $('tr#searchTr input[type=submit]').click();

        });

        $('tr#searchTr input[type=submit]').click(function(){



            var formData = {};

            $('tr#searchTr input[type=text], form#searchCond input[type=hidden], tr#searchTr select').each(function(){
                formData[this.name] = $(this).val();
            });

            var searchData = {
                'number' : (formData.c_number != "") ? formData.c_number : null,
                //'catContract' : formData.catContract.join(','),
                'catContract' : formData.catContract,
                'catContractStatus' : formData.catContractStatus,
                'paymentWay' : formData.paymentWay,
                'person' : (formData.person != "") ? formData.person : null,
                'partner' : (formData.partner != "")? formData.partner : null,
                'creator' : (formData.creator != "")? formData.creator : null,
                'startDateFrom' : formData.startDateFrom,
                'startDateTo' : formData.startDateTo,
                'isApp' : (formData.appDate == "yes")? true : (formData.appDate == "no"? false : null),
                'isPrinted' : (formData.printDate == "yes")? true : (formData.printDate == "no"? false : null),
                'isPaid' : (formData.payDate == "yes")? true : (formData.payDate == "no"? false : null),
                'orderColumn' : formData.orderColumn,
                'orderType' : formData.orderType
            };

            //alert(JSON.stringify(searchData));

            $.post(
                urlPrefix + "contracts?ajaxList",
                {
                    'filter' : JSON.stringify(searchData)
                },
                function(d){
                   // var win = window.open();
                   // win.document.write(d);

                    var tb = $('table tbody',d).get(0);

                    if(tb != null){
                        $('table tbody').html($(tb).html());
                    }
                    else{
                        $('table tbody').html('');
                    }
                }
            );

            return false;
        });
    };
})(ajaxMessages, urlPrefix);

$(document).ready(searchContract);