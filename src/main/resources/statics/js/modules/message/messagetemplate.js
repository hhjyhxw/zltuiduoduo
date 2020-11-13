$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'message/messagetemplate/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '模板标题', name: 'titile', index: 'titile', width: 80 },
			{ label: '消息头部描述', name: 'first', index: 'first', width: 80 }, 			
			{ label: '关键字1', name: 'keyword1', index: 'keyword1', width: 80 }, 			
			{ label: '关键字2', name: 'keyword2', index: 'keyword2', width: 80 }, 			
			{ label: '关键字3', name: 'keyword3', index: 'keyword3', width: 80 }, 			
			{ label: '关键字4', name: 'keyword4', index: 'keyword4', width: 80 }, 			
			{ label: '关键字5', name: 'keyword5', index: 'keyword5', width: 80 }, 			
			{ label: '消息底部描述', name: 'remark', index: 'remark', width: 80 }, 			
			{ label: '微信模板id', name: 'templateId', index: 'template_id', width: 80 }, 			
			{ label: '点击模板跳转地址', name: 'visitUrl', index: 'visit_url', width: 80 }, 			

			{ label: '消息发送处理器', name: 'dealZclass', index: 'deal_zclass', width: 80 },
			{ label: '状态', name: 'status', width: 60, formatter: function(value, options, row){
                                                                 return value == '0' ?'<span class="label label-success">停用</span>' :
                                                                  (value == '1' ?'<span class="label label-success">启用</span>' :
                                                                  (value == '2' ?'<span class="label label-success">未知</span>' :'未知'))
                                                                                    }},
//			{ label: '是否已发送（0未发送 1已发送）', name: 'sendStatus', index: 'send_status', width: 80 }

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
		messageTemplate: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.messageTemplate = {};
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
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.messageTemplate.id == null ? "message/messagetemplate/save" : "message/messagetemplate/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.messageTemplate),
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
                        url: baseURL + "message/messagetemplate/delete",
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
			$.get(baseURL + "message/messagetemplate/info/"+id, function(r){
                vm.messageTemplate = r.messageTemplate;
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