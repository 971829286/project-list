(function(win,$)
{
    $body=$('body');
    var temp = '';
    var lib = {
        deleteEmptyProperty:function(object){
            for (var i in object) {
                var value = object[i];
                // sodino.com
                // console.log('typeof object[' + i + ']', (typeof value));
                if (typeof value === 'object') {
                    if (Array.isArray(value)) {
                        if (value.length == 0) {
                            delete object[i];
                            console.log('delete Array', i);
                            continue;
                        }
                    }
                    lib.deleteEmptyProperty(value);
                    if (lib.isEmpty(value)) {
                        console.log('isEmpty true', i, value);
                        delete object[i];
                        console.log('delete a empty object');
                    }
                } else {
                    if (value === '' || value === null || value === undefined) {
                        delete object[i];
                        console.log('delete ', i);
                    } else {
                        console.log('check ', i, value);
                    }
                }
            }
        },
        isEmpty:function(object) {
            for (var name in object) {
                return false;
            }
            return true;
        },
        httpPost:function(url,success,error)
		{
			return $.ajax(
			{
				url:url,
				type:'POST',
				headers:
				{
					'Content-Type':'application/json; charset=UTF-8'
				},
				success:success,
				error:error
			});
        },
        httpGet:function(url,success,type,error)
		{
			return $.ajax(
			{
				url:url,
				type:'GET',
				dataType: type?type:'json',
				withCredentials: true,
				success:success,
				error:error
			});
        },
        formatTime:function(time){
            var temp = new Date(time);
            var year = temp.getFullYear();
            var month = temp.getMonth()+1;
            var day = temp.getDate();
            return year+'.'+month+'.'+day;
        },
    };
    var common =
    {
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
                    prevButton:'.swiper-button-prev',
                    nextButton:'.swiper-button-next',
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
        searchFocus:function()
        {
        	$('.hy-search-wrap').each(function(i,item){
        		$(item).find('input').focus(function(){
	        		$(item).addClass('active');
	        	})
	        	.blur(function(){
	        		$(item).removeClass('active');
	        	})
        	})
        },
        adClose:function()
        {
            $body.on('click','.ad-close',function()
            {
                $('.in-ad').slideUp(1000);
            });
        },
        indexListener:function(){
            $('#headerSearch').on('click',function(){
                temp = $('#headerInput').val();
                localStorage.setItem('searchVal', temp);
                if(window.location.pathname !== '/meetingList'){
                    window.location.href = '/meetingList';
                }else{
                    $('#headerInput').val('');
                    if(temp == ''){
                        $('#searchVal').val('');
                    }
                    meetingList.getData();
                }
            });
            
        },
    };
    var meetingList = {
        isSort:true,
        getCategoryList:function(){
            lib.httpGet('/api/activityType/list',function(res){
                if(res.code === 0){
                    res.data.forEach(function(e){
                        $('#categoryUl').append('<li><a href="#">'+e.typeName+'</a></li>');
                    });
                    $('#categoryUl').append('<li><a href="#" >所有类别</a></li>');
                }else{
                    console.log('error in getCategoryList');
                }
            });
        },
        getData: function(num,select){
            var range = null;
            switch($('#timeVal').html()){
                case '今天':
                    range = '+0';
                    break;
                case '明天':
                    range = '+1';
                    break;
                case '未来三天':
                    range = '+3';
                    break;
                case '未来一周':
                    range = '+7';
                    break;
                case '所有时间':
                    range = null;
                    break;
            }
            if(window.localStorage.getItem('searchVal') !== ''){
                debugger;
                $('#searchVal').val(window.localStorage.getItem('searchVal'));
                window.localStorage.setItem('searchVal','');
            }
            var queryData = {
                field:$('#searchField').html() === '活动'?'activityTitle':'nickName',
                pageNum:1,
                pageSize:8,
                keyword:$('#searchVal').val(),
                typeName:$('#categoryVal').html() === '所有类别'?null:$('#categoryVal').html(),
                range: range,
                isSort:select?true:$('#searchVal').val() === ''?true:false
            };
            if(num){
                queryData.pageNum = num;
            }
            lib.deleteEmptyProperty(queryData);
            $('#listContainer').empty();
            lib.httpPost('/api/search/activity/conditionalQuery?'+jQuery.param(queryData),function(res){
                if(res.code === 0){
                    $('.nav-navigation').css('display', 'block');
                    res.data.list.forEach(function(e){
                        $('#listContainer').append(
                        '<li class="box">'
                        // +'<div class="activity-media" onclick="window.location.href=\'/web/activity/'+ e.id +'\'">'
                        +'<div class="activity-media" onclick="window.open(\'/web/activity/'+ e.id +'\')">'
                        +    '<div class="activity-banner">'
                        +    '<img src="'+ e.activityBannerMobile+'">'
                        +    '</div>'
                        +    '<div class="activity-info">'
                        +    '<div class="activity-title">'+e.activityTitle+'</div>'
                        +    '<div class="activity-date"><span>'+lib.formatTime(e.startTime)+'-'+lib.formatTime(e.endTime)+'</span></div>'
                        +    '<div class="activity-detail">'
                        +        '<span class="look"  data-toggle="tooltip" data-placement="top" title="参与人数">'
                        +        '<svg class="icon" aria-hidden="true">'
                        +            '<use xlink:href="#icon-guankan"></use>'
                        +        '</svg>'
                        +        '<span>'+e.statistics.watchCount+'</span>'
                        +        '</span>'
                        +        '<span class="star" data-toggle="tooltip" data-placement="top" title="收藏数">'
                        +        '<svg class="icon" aria-hidden="true">'
                        +            '<use xlink:href="#icon-shoucang"></use>'
                        +        '</svg>'
                        +        '<span>'+e.statistics.collectCount+'</span>'
                        +        '</span>'
                        +    '</div>'
                        +    '</div>'
                        +'</div>'
                        +'<div class="avatar" onclick="window.open(\'/web/usercenter/activity/'+e.userId+'\')">'
                        +    '<img class="img-circle" src="'+e.avatar+'" />'
                        +    '<span class="user-name">'+(e.nickName || e.userName)+'</span>'
                        +'</div>'
                        +'</li>'
                        );
                    });
                    // 分页
                    $("#paginationUl").pagination({
                        pageCount: res.data.pages,
                        totalData: res.data.total,
                        showData: 8,
                        current: queryData.pageNum,
                        prevContent:'&laquo;',
                        nextContent:'&raquo;',
                        activeCls: 'active',
                        coping: true,
                        prevCls: 'prev',
                        nextCls: 'next',
                        // homePage: '首页',
                        // endPage: '尾页',
                        callback: function(api) {
                            var data = {
                                pageNum: api.getCurrent(),
                                pageSize: 6,
                            }
                            // console.log(data.pageNum, '当前页数12345678');
                            queryData.pageNum = data.pageNum;
                            meetingList.getData(data.pageNum);
                            // location.hash = '#page=' + data.pageNum;
                            // sessionStorage.setItem('page', data.pageNum);
                        }
                    })
                }else{
                    // $('li').remove('.numPageDom');
                    $('#listContainer').append('<p style="color:#6666">'+res.msg+'</p>');
                    $('.nav-navigation').css('display', 'none');
                }
            });
        },
        toggleField:function(){
            $('#searchUl').on('click',function(e){
                var str =  e.target.innerHTML;
                $('#searchField').html(str);
            });
        },
        toggleCategory: function(){
            $('#categoryUl').on('click',function(e){
                var str =  e.target.innerHTML;
                $('#categoryVal').html(str);
            });
        },
        toggleTime:function(){
            $('#timeUl').on('click',function(e){
                var str =  e.target.innerHTML;
                $('#timeVal').html(str);
            });
        },
        searchRun:function(){
            $('#searchButton').on('click',function(){
                sessionStorage.setItem('page', 1);
                meetingList.getData();
            })
        },
        pageJump:function(){
            // 上一页 和下一页的跳转
            console.log('meetList.pageJump');
            // $('#paginationUl').on('click',function(e){
            //     var target = {};
            //     if (e.target.localName === 'span') {
            //         target = e.target.parentNode;
            //     } else {
            //         target = e.target;
            //     }
            //     var str = target.dataset.num;
            //     // 上一页
            //     if (target.className === 'pageButton preButton') {
            //         if (parseInt(str) > 1) {
            //             str = parseInt(str) - 1;
            //         } else {
            //             str = 1;
            //         }
            //     // 下一页
            //     } else if (target.className === 'pageButton nextButton') {
            //         var length = $('#paginationUl').children().length - 2;
            //         if (parseInt(str) < length) {
            //             str = parseInt(str) + 1;
            //         } else {
            //             str = length;
            //         }
            //     }
            //     // 当前页面添加class
            //     var pages = []
            //     pages = $('#paginationUl').children();
            //     for(var i = 1; i < pages.length - 1; i++) {
            //         if(str == i) {
            //             pages[i].className = 'numPageDom active'
            //         } else {
            //             pages[i].className = 'numPageDom'
            //         }
            //     }
            //     $('.pageButton').attr('data-num', str);
            //     meetingList.getData(str);
            // });
        },
        enterButtonListener:function(){
            var _this = this;
            $('#categoryUl').click(function() { 
                // do something after the div content has changed
                meetingList.getData(1,true);
            });
            $('#timeUl').click(function() { 
                // do something after the div content has changed 
                meetingList.getData(1,true);
            });
            
            document.onkeydown = function(e) {
                // 兼容FF和IE和Opera  
                var theEvent = e || window.event;
                var code = theEvent.keyCode || theEvent.which || theEvent.charCode;  
                var activeElementId = document.activeElement.id;//当前处于焦点的元素的id  
                if (code == 13 && activeElementId == 'searchVal') { 
                    meetingList.getData();
                    return false;  
                }
                return true;  
            } 
        }
    };

    var controller=
    {
        home:function()
        {
            common.slider();
            common.searchFocus();
            common.adClose();
        },
        about:function()
        {
            $body.on('click','.aaa',function()
            {

            });
        },
        meetingList:function(){
            //监听
            // var page = sessionStorage.getItem('page') ? sessionStorage.getItem('page') : null
            meetingList.getData();
            meetingList.toggleField();
            meetingList.toggleCategory();
            meetingList.toggleTime();
            meetingList.searchRun();
            meetingList.pageJump();
            //获取数据
            meetingList.getCategoryList();
            meetingList.enterButtonListener();
        },
    };

    function init()
    {
        console.log('初始化')
        common.indexListener();
        // debugger
        var page=$('[data-page]').data('page');
        $('[data-toggle="tooltip"]').tooltip();
        $('.nav .'+page).addClass('active');
        $('.meetingList').click(function () {
            sessionStorage.setItem('page', 1)
        });
        if(controller[page])
        {
            controller[page]();
        }
    }
    win.init=init;
})(window,jQuery);

init&&init();