$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'message/messagesendrecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '本地消息模板id(活动id)', name: 'messageId', index: 'message_id', width: 80 }, 			
			{ label: '用户openid', name: 'openid', index: 'openid', width: 80 }, 			
			{ label: '发送状态', name: 'status', width: 60, formatter: function(value, options, row){
                                                     return value == '0' ?'<span class="label label-success">未发送</span>' :
                                                      (value == '1' ?'<span class="label label-success">已发送</span>' :
                                                      (value == '2' ?'<span class="label label-success">发送失败</span>' :'未知'))
                                                                        }},
			{ label: '发送结果描述', name: 'msg', index: 'msg', width: 80 }, 			
			{ label: '发送时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '更新时间', name: 'modifyTime', index: 'modify_time', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#icloudapp',
	data:{
		showList: true,
		title: null,
		messageSendrecord: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.messageSendrecord = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		  //导入
                btnInpomt: function (event) {
                    $('#btnInpomt').button('loading').delay(1000).queue(function() {
                        var viurl = "retail/retailconfirn/importusers?url="+vm.userurl;
                        $.ajax({
                            type: "get",
                            url: baseURL + viurl,
                            contentType: "application/json",
                            // data: {url:vm.userurl},
                            success: function(r){
                                if(r.code === 0){
                                    layer.msg("操作成功", {icon: 1});
                                    vm.reload();
                                    $('#btnInpomt').button('reset');
                                    $('#btnInpomt').dequeue();
                                }else{
                                    layer.alert(r.msg);
                                    $('#btnInpomt').button('reset');
                                    $('#btnInpomt').dequeue();
                                }
                            }
                        });
                    });
                },
                //导出
                btndownload: function (event) {
                    var pagesize = 50000;
                    var viurl = baseURL + "retail/retailconfirn/downLoaduserlist?userName?="+vm.q.userName+"&status="+vm.q.status+"&page="+vm.q.page+"&limit="+pagesize;
                    window.location.href = viurl;
                },

		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.messageSendrecord.id == null ? "message/messagesendrecord/save" : "message/messagesendrecord/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.messageSendrecord),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "message/messagesendrecord/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "message/messagesendrecord/info/"+id, function(r){
                vm.messageSendrecord = r.messageSendrecord;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});