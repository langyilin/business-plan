'use strict'

angular.module('app.visit', []).config(function ($stateProvider) {
	$stateProvider.state('app.visit', {
        abstract: true
    })
    .state('app.visit.list', {
        url: '/visit/list',
        views: {
            "content@app": {
                templateUrl: 'visit/visit/list.mvc',
                controller: "visitListCtrl"
            }
        },
        resolve: {
            srcipts: function(lazyScript){
                return lazyScript.register([
                    'scripts/visit/visitList.js'
                ])
            }
        }
    })
    .state('app.visit.listDb', {
        url: '/visit/listDb',
        views: {
            "content@app": {
                templateUrl: 'visit/visit/listDb.mvc',
                controller: "visitListDbCtrl"
            }
        },
        resolve: {
            srcipts: function(lazyScript){
                return lazyScript.register([
                    'scripts/visit/myVisitList.js'
                ])
            }
        }
    })
    .state('app.visit.form', {
        url: '/visit/form/:id/:taskId',
        views: {
            "content@app": {
                templateUrl: 'visit/visit/form.mvc',
                controller: "visitFormCtrl"
            }
        },
        resolve: {
            srcipts: function(lazyScript){
                return lazyScript.register([
                    'scripts/visit/visitForm.js'
                ])

            }
        }
    })
});
