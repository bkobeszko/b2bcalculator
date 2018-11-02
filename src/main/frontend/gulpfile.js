var
  gulp  = require('gulp'),
  semantic_ui_watch = require('./semantic/tasks/watch'),
  semantic_ui_build = require('./semantic/tasks/build'),
  semantic_ui_build_js = require('./semantic/tasks/build/javascript'),
  semantic_ui_build_css = require('./semantic/tasks/build/css'),
  semantic_ui_build_assets = require('./semantic/tasks/build/assets')
;

gulp.task('semantic_ui_watch', semantic_ui_watch);
gulp.task('semantic_ui_build', semantic_ui_build);
gulp.task('semantic_ui_build_js', semantic_ui_build_js);
gulp.task('semantic_ui_build_css', semantic_ui_build_css);
gulp.task('semantic_ui_build_assets', semantic_ui_build_assets);