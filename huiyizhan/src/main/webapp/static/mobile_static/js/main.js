(function (win, $) {
    $body = $('body');
    var lib = {
        httpGet:function(url, success, type, error) {
            return $.ajax({
                url: url,
                type:'GET',
                dataType: type?type:'json',
                withCredentials: true,
                success: success,
                error: error
            });
        },
        httpPost:function(url, success, error)
        {
            return $.ajax(
                {
                    url: url,
                    type:'POST',
                    headers:
                    {
                        'Content-Type':'application/json; charset=UTF-8'
                    },
                    success: success,
                    error: error
                });
        },
    };
    var common = {
        hasMoreData: true,
        curPageNum:1,
        slider:function()
        {
            $.each($('.swiper-container'),function(index,item)
            {

                var $this=$(this);
                console.log(this.dataset);
                console.log($this.data('slidesperview'));
                var slidesPerView = $this.data('slidesperview')||1;
                var slidesPerGroup = $this.data('slidespergroup')||1;
                var pagination = this.querySelector('.swiper-pagination');
                var $slider = $this.swiper(
                    {
                        slidesPerView : slidesPerView,
                        slidesPerGroup : slidesPerGroup,
                        direction: 'horizontal',
                        loop: true,
                        initialSlide :1,
                        autoplay : 3500,
                        autoplayDisableOnInteraction : false,
                        preventClicks : false,
                        paginationClickable :true,
                        pagination:pagination,
                        //prevButton:'.swiper-button-prev',
                        //nextButton:'.swiper-button-next',
                    });
                $slider.on('mouseenter',function()
                {
                    $slider.stopAutoplay();
                }).on('mouseleave',function()
                {
                    $slider.startAutoplay();
                });
            });
        },
        judgeCondition: function() {
            if (window.navigator.userAgent.indexOf('MicroMessenger') === -1) {
                $('#menuBox .router').addClass('notWechat');
            }
        },
        showMask: function() {
            $('#mask').addClass('maskZ').addClass('maskO');
            $('header').removeClass('hidden');
            $('body').on('touchmove', common.preventDefaultFn);
            $('#mask').on('touchmove',common.preventDefaultFn);
            $('#menuBox').on('touchmove',common.preventDefaultFn);
         
        },
        closeMask: function() {
            $('#mask').removeClass('maskO');
            $('body').off('touchmove', common.preventDefaultFn);
            $('#mask').off('touchmove',common.preventDefaultFn);
            $('#menuBox').off('touchmove',common.preventDefaultFn);
            setTimeout(function() {
                $('header').addClass('hidden');
                $('#mask').removeClass('maskZ');
            }, 300);
        },
        preventDefaultFn: function(event){
            event.preventDefault();
        },
        showMenu: function() {
            common.showMask();
            $('#menuBox').removeClass('slideOutRight').addClass('slideInRight show');

        },
        closeMenu: function() {
            common.closeMask();
            $('#menuBox').removeClass('slideInRight').addClass('slideOutRight');
        },
        showSearch: function() {
            common.showMask();
            $('#searchBox').removeClass('slideOutUp').addClass('slideInDown show')
        },
        closeSearch: function() {
            common.closeMask();
            $('#searchBox').removeClass('slideInDown').addClass('slideOutUp');
        },
        headerInit: function() {
            $('#menu').on('click', function() {
                common.showMenu();
            });
            $('#menuClose').on('click', function() {
                common.closeMenu();
            });
            $('#search').on('click', function() {
                // common.showSearch();
                window.location.href = "/web/search";
            });
            $('#searchClose').on('click', function() {
                common.closeSearch();
            });
            $('#mask').on('click', function() {
                common.closeMenu();
                common.closeSearch();
            });
            // $('#searchInput').on('focus',function(){
            //     debugger;
            //     window.location.href = "/web/search";
            // });
        },
        getActivityCategoryList: function() {
            lib.httpGet('/api/activityType/list', function(res){
                if(res.code === 0){
                    var content='<li class="selectLi action" data-i="all">所有类别</li>';
                    res.data.forEach(function(e){
                        content+='<li class="selectLi" data-i="'+e.typeName+'">' + e.typeName + '</li>';
                    });
                    $('#mobileCategoryUl').append(content);
                }else{
                    console.log('error in getCategoryList');
                }
            });
        },
    };
    var controller = {
        showMask: function() {
            $('#selectMask').addClass('maskZ').addClass('maskO');
        },
        closeMask: function() {
            $('#selectMask').removeClass('maskO');
            setTimeout(function() {
                $('#selectMask').removeClass('maskZ');
            }, 300);
        },
        showSelectCategory: function() {
            controller.showMask();
            $('#selectCategoryHook').removeClass('slideOutUp').addClass('slideInDown show');
            $('#selectCategoryBtn').addClass('selectItemHighLight');
            controller.selectView(true);
        },
        closeSelectCategory: function() {
            controller.closeMask();
            $('#selectCategoryHook').removeClass('slideInDown').addClass('slideOutUp');
            $('#selectCategoryBtn').removeClass('selectItemHighLight');
            controller.selectView(false);
        },
        showSelectTime: function() {
            controller.showMask();
            $('#selectTimeHook').removeClass('slideOutUp').addClass('slideInDown show');
            $('#selectTimeBtn').addClass('selectItemHighLight');
            controller.selectView(true);
        },
        closeSelectTime: function() {
            controller.closeMask();
            $('#selectTimeHook').removeClass('slideInDown').addClass('slideOutUp');
            $('#selectTimeBtn').removeClass('selectItemHighLight');
            controller.selectView(false);
        },
        selectView: function (flag) {
            if (flag) {
                $('html').addClass('windowNoScroll');
                $('.selectBox').addClass('selectBoxScroll');
            } else {
                $('html').removeClass('windowNoScroll');
                $('.selectBox').removeClass('selectBoxScroll');
            }
        },
        requestData:function(me){
            var range = $('#mobileTimeUl .action').data("i");
            var type = $('#mobileCategoryUl .action').data("i");
            var queryData = {
                typeName: type === 'all' ? null : type,
                range: range,
                pageSize: 10,
                pageNum: this.curPageNum
            };
            lib.httpPost('/api/search/activity/conditionalQuery?' + jQuery.param(queryData), function(res){
                if(res.code === 0){
                    controller.dataAdd(res.data.list);
                    if (!res.data.hasNextPage) {
                        common.hasMoreData = false;
                        if(!me) $('.dropload-down').children('div').html('没有更多数据');
                    }
                } else {
                    $('#activityListUl').html('<p class="dropload-load">' + res.msg + '</p>');
                    $('.dropload-down').remove();
                }
                //每次数据加载完，必须重置
                if (me) {
                    me.resetload();
                }
            }, function(err) {
                console.log(err);
                // 即使加载出错，也得重置
                if (me) {
                    me.resetload();
                }
            });
        },
        dataAdd:function(list){
            var str='';
            list.forEach(function(e){
                var time=formatDate(new Date(e.startTime),'MM月dd日 hh:mm');
                str+='<li class="activityListLi border-1px-b"><a href="/web/activity/'+e.id+'"><div class="activityList"><div class="listImg"><img src=' +
                    e.activityBannerMobile + '></div><div class="listContent"><p class="name">' +
                    e.activityTitle + '</p><p class="date">' +
                    time + '</p></div></div></a></li>'
            });
            $('#activityListUl').append(str);
        },
        getData: function(me){
            //debugger;
            this.curPageNum = 1;
            common.hasMoreData = true;
            this.requestData();
            controller.getDataByDrop();
        },
        getDataByDrop: function () {
            $('.activityListView-hook').dropload({
                scrollArea : win,
                autoLoad: false,
                domDown: {
                    domClass : 'dropload-down',
                    domRefresh : '<div class="dropload-refresh">加载中...</div>',
                    domLoad : '<div class="dropload-load">加载中...</div>',
                    domNoData : '<div class="dropload-noData">没有更多数据</div>',
                },
                loadDownFn : function(me) {
                    if (common.hasMoreData) {
                        controller.curPageNum += 1;
                        controller.requestData(me);
                    } else {
                        me.resetload();
                        me.noData(true);
                        //$('.dropload-down').children('div').html('已经到底啦~');
                    }
                }
            });
        },
        toggleCategory: function(){
            $('#mobileCategoryUl').on('click', function(e){
                if (e.target.innerHTML !== $('#selectCategoryBtn').children('span').html()) {
                    $('#mobileCategoryUl li.selectLi').removeClass('action');
                    e.target.className = 'selectLi action';
                    var str = e.target.innerHTML;
                    $('#selectCategoryBtn').children('span').html(str);
                    $('#activityListUl').empty();
                    $('.dropload-down').remove();
                    controller.getData();
                }
                controller.closeSelectCategory();
            });
        },
        toggleTime:function(){
            $('#mobileTimeUl').on('click',function(e){
                if (e.target.innerHTML !== $('#selectTimeBtn').children('span').html()) {
                    $('#mobileTimeUl li.selectLi').removeClass('action');
                    e.target.className = 'selectLi action';
                    var str = e.target.innerHTML;
                    $('#selectTimeBtn').children('span').html(str);
                    $('#activityListUl').empty();
                    $('.dropload-down').remove();
                    controller.getData();
                }
                controller.closeSelectTime();
            });
        },
        activityInit: function() {
            $('#selectCategoryBtn').on('click', function() {
                if ($('#selectCategoryHook').hasClass('slideInDown')) {
                    controller.closeSelectCategory();
                } else if ($('#selectTimeHook').hasClass('slideInDown'))  {
                    controller.closeSelectTime();
                    setTimeout(function() {
                        controller.showSelectCategory();
                    }, 300);
                } else {
                    controller.showSelectCategory();
                }
            });
            $('#selectTimeBtn').on('click', function() {
                if ($('#selectTimeHook').hasClass('slideInDown')) {
                    controller.closeSelectTime();
                } else if ($('#selectCategoryHook').hasClass('slideInDown'))  {
                    controller.closeSelectCategory();
                    setTimeout(function() {
                        controller.showSelectTime();
                    }, 300);
                } else {
                    controller.showSelectTime();
                }
            });
            $('#selectMask').on('click', function() {
                controller.closeSelectCategory();
                controller.closeSelectTime();
            });
        }
    };
    function init() {
        var page = $('[data-page]').data('page');
        common.judgeCondition();
        common.headerInit();
        common.getActivityCategoryList();
        common.slider();
        controller.activityInit();
        controller.getData();
        //controller.getDataByDrop();
        controller.toggleCategory();
        controller.toggleTime();
        if(controller[page]){
            controller[page]();
        }
    }
    win.init = init;
    function formatDate(date, format) {
        if(format === undefined){
            format = date;
            date = new Date();
        }
        var map = {
            "M": date.getMonth() + 1, //月份
            "d": date.getDate(), //日
            "h": date.getHours(), //小时
            "m": date.getMinutes(), //分
            "s": date.getSeconds(), //秒
            "q": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
        };
        format = format.replace(/([yMdhmsqS])+/g, function(all, t){
            var v = map[t];
            if(v !== undefined){
                if(all.length > 1){
                    v = '0' + v;
                    v = v.substr(v.length-2);
                }
                return v;
            }
            else if(t === 'y'){
                return (date.getFullYear() + '').substr(4 - all.length);
            }
            return all;
        });
        return format;
    };
})(window, jQuery);

init && init();
