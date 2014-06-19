(function($) {
    $(function() {
        var $window = $(window);
        var $temLeft = $('#temLeft');
        var $temLeft2 = $('#temLeft2');
        var $temHead = $('#temHead');
        var $temRight = $('#temRight');
        var $complet = $('.complet');

        $(".temIcon2").ready(function() {
            $("#temLeft").show();
            $("#temLeft2").hide();
            $complet.css({
                'width': '100%',
                'height': $window.height() - $temHead.height() + 'px'
            });

            $temLeft.css({
                'height': $complet.height() + 'px'
            });
            $temRight.css({
                'width': $complet.width() - $temLeft.width() + 'px',
                'height': $complet.height() + 'px'
            });
        });


        $window.resize(function() {
            $(".temIcon").ready(function() {
                $("#temLeft2").hide();
                $("#temLeft").show();
                $temLeft.css({
                    'height': $complet.height() + 'px'
                });
                $temRight.css({
                    'width': $complet.width() - $temLeft.width() + 'px',
                    'height': $complet.height() + 'px'
                });
            });
        });

        $(".temIcon2").click(function() {
            $("#temLeft").show();
            $("#temLeft2").hide();

            $temRight.css({
                'width': $complet.width() - $temLeft.width() + 'px',
                'height': $complet.height() + 'px'
            });
        });


        $(".temIcon").click(function() {
            $("#temLeft").hide();
            $("#temLeft2").show();
            $temLeft2.css({
                'height': $complet.height() + 'px'
            });
            $temRight.css({
                'width': $complet.width() - $temLeft2.width() + 'px',
                'height': $complet.height() + 'px'
            });
        });
    });
}).call(this, jQuery);