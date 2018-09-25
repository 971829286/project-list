define(function(require, exports)
{
	var util=
	{
		url:function(path) {
			return host + path;
		},
		httpPut:function(url,data,success,type,error)
		{
			return $.ajax(
			{
				url:url,
				headers:
				{
					'Content-Type':'application/json; charset=UTF-8'
				},
				data:data?JSON.stringify(data):undefined,
				type:'PUT',
				dataType: type?type:'json',
				success:success,
				error:error
			});
		},
		httpDelete:function(url,success,type,error)
		{
			return $.ajax(
			{
				url:url,
				type:'DELETE',
				dataType: type?type:'json',
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
		httpPost:function(url,data,success,type,error)
		{
			return $.ajax(
			{
				url:url,
				type:'POST',
				headers:
				{
					'Content-Type':'application/json; charset=UTF-8'
				},
				data:data?JSON.stringify(data):undefined,
				dataType: type?type:'json',
				success:success,
				error:error
			});
		},
		httpPostForm:function(url,data,success,type,error)
		{
			return $.ajax(
			{
				url:url,
				type:'POST',
				data:data,
				dataType: type?type:'json',
				success:success,
				error:error
			});
		},
		httpPatch:function(url,data,success,type,error)
		{
			return $.ajax(
			{
				url:url,
				type:'PATCH',
				headers:
				{
					'Content-Type':'application/json; charset=UTF-8'
				},
				data:data?JSON.stringify(data):undefined,
				dataType: type?type:'json',
				success:success,
				error:error
			});
		}
	};
})