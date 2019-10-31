angular.module('Gesweb.filters', [])
    // Genera un num. determinado de elementos en un array vacío: <li ng-repeat="n in [] | range:10">test</li>
    .filter('range', function() {
        return function(input, total) {
            if (!input) return null;
            total = parseInt(total);
            for (var i = 0; i < total; i++)
                input.push(i);
            return input;
        };
    })
    // Escapar HTML antes de insertarlo en el modelo
    .filter('escapeHTML', function(text) {
        if (text) {
            return text.
            replace(/&/g, '&amp;').
            replace(/</g, '&lt;').
            replace(/>/g, '&gt;').
            replace(/'/g, '&#39;').
            replace(/"/g, '&quot;');
        };
        return '';
    })
    // Ejemplo de uso: registro.id = 'rec-533' -> template: {{ registro.id | split:'-':1 }}
    .filter('split', function() {
        return function(input, splitChar, splitIndex) {
            // do some bounds checking here to ensure it has that index
            return input.split(splitChar)[splitIndex];
        };
    })
    .filter('indent', function() {
        return function(input, key) {
            return key == true ? ' - ' + input : input;
        };
    })
    .filter('pintarFiltros', function($sce) {
        return function(arr, key) {
            var string = '';
            _.map(arr, function(v, o) {
                string += "<label class=\"label label-info\">" + v.key + ": " + v.value + "</label> ";
            });
            return $sce.trustAsHtml(string);
        };
    })
    .filter('objetoEnListaHTML', function($sce) {
        return function(obj, key) {
            var list = '<ul class=\"nomargin fnd-gris-claro\">'
            _.each(obj, function(value, key, obj) {
                list += '<li><strong>' + key + ':</strong> ' + value + '</li>';
            });
            list += '</ul>';
            return $sce.trustAsHtml(list);
        };
    })
    .filter('trusted', ['$sce', function($sce) {
        return function(url) {
            return $sce.trustAsResourceUrl(url);
        };
    }])
    .filter('typeof', function() {
        return function(obj) {
            return typeof obj
        };
    });
