
/**
 * <p>
 *    要安装 插件AnsiColor，这样才能使用ansiColor()方法，可以在片段生成器查看更多的用法。
 * </p>
 * @author Dylan
 * @since 2021-12-29
 * @version 1.0.0
 */

/**
 *
 * @param value 需要输出的日志，默认颜色
 * @return
 */
def PrintMes(value){
    PrintMes(value,'blue')
}

/**
 * 自定义颜色
 * @param value 需要输出的日志
 * @param color 指定颜色
 * @return
 */
def PrintMes(value,color){
    colors = ['red'   : "\033[40;31m >>>>>>>>>>>${value}<<<<<<<<<<< \033[0m",
              'blue'  : "\033[47;34m ${value} \033[0m",
              'green' : "[1;32m>>>>>>>>>>${value}>>>>>>>>>>[m",
              'green1' : "\033[40;32m >>>>>>>>>>>${value}<<<<<<<<<<< \033[0m" ]
    ansiColor('xterm') {
        println(colors[color])
    }
}


