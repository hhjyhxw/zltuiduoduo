$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'zltdd/zltddrecommend/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '用户昵称', name: 'nickname', index: 'nickname', width: 80 }, 			
			{ label: 'openid', name: 'openid', index: 'openid', width: 80 }, 			
			{ label: '头像', name: 'headimgurl', index: 'headimgurl', width: 80 }, 			
			{ label: '活动id', name: 'activityId', index: 'activity_id', width: 80 }, 			
			{ label: '父类id', name: 'parentId', index: 'parent_id', width: 80 }, 			
			{ label: '用户类型', name: 'userType', index: 'user_type', width: 80 }, 			
			{ label: '0为无无限发展', name: 'maxNum', index: 'max_num', width: 80 }, 			
			{ label: '已发展人数', name: 'readyedNum', index: 'readyed_num', width: 80 }, 			
			{ label: '场景码(推广参数)', name: 'mysceneId', index: 'myscene_id', width: 80 }, 			
			{ label: '父类场景码（父类推广参数）', name: 'parentSceneId', index: 'parent_scene_id', width: 80 }			
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
		zltddRecommend: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.zltddRecommend = {};
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
                var url = vm.zltddRecommend.id == null ? "zltdd/zltddrecommend/save" : "zltdd/zltddrecommend/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.zltddRecommend),
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
                        url: baseURL + "zltdd/zltddrecommend/delete",
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
			$.get(baseURL + "zltdd/zltddrecommend/info/"+id, function(r){
                vm.zltddRecommend = r.zltddRecommend;
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