package com.javayh.jenkins.steps


/**
 * <p>
 *    PipelineSteps
 * </p>
 * @author Dylan
 * @since 2021-12-29
 * @version 1.0.0
 */
class PipelineSteps {
    def steps
    def ignoreQualityGate

    PipelineSteps(steps) {
        this.steps = steps
    }

    def ignoreQualityGate() {
        this.ignoreQualityGate = true
    }

    /**
     * Requires the job to be a child of a folder
     *
     * @return the folder name
     */
    def multiBranchDisplayName() {
        return steps.env.JOB_NAME[0..steps.env.JOB_NAME.indexOf("/") - 1]
    }

    /**
     * Execute given mvn commands
     * 指定需要指定的maven 命令
     * @param mvnCommand 执行参数
     * @return
     */
    def executeMaven(mvnCommand) {
        steps.sh "mvn $mvnCommand"
    }

    /**
     * 执行maven install 默认跳过测试
     * @param mvnParams 执行参数
     * @return
     */
    def mavenInstall(mvnParams) {
        executeMaven("clean install -Dmaven.test.failure.ignore=true $mvnParams")
    }


    /**
     *
     * @param mvnParams
     * @return
     */
    def mavenDeploy(mvnParams) {
        executeMaven("deploy -Dmaven.test.skip=true -DskipTests $mvnParams")
    }

    def mavenSite(mvnParams) {
        executeMaven("site site:deploy -DskipTests=true $mvnParams")
    }

    def mavenVerify(mvnParams) {
        executeMaven("clean verify $mvnParams")
    }

    def archiveArtifacts(includes, excludes) {
        steps.archiveArtifacts(artifacts: includes, excludes: excludes)
    }

    def getMavenReleaseVersion(mvnParams) {
        executeMaven("build-helper:parse-version versions:set versions:commit " +
                "-DnewVersion=$steps.env.NEW_RELEASE_VERSION -DprocessDependencies=true " +
                "-DprocessPlugins=false -DprocessParent=true ${mvnParams}")
    }

    def setMavenSnapshotVersion(mvnParams) {
        executeMaven("build-helper:parse-version versions:set versions:commit -DnewVersion=$steps.env.NEXT_SNAPSHOT_VERSION" +
                " -DprocessDependencies=true -DprocessPlugins=false -DprocessParent=true $mvnParams")
    }

    def recordJunitResults() {
        steps.junit '**/target/surefire-reports/*.xml'
    }

    /**
     * Checks if all String parameters are not empty
     * Except for the ones defined in the exclude parameter
     *
     * @param excludes defines String job parameters which are allowed to be empty
     * @return
     */
    def checkParameters(Collection excludes) {
        steps.params.each { key, value ->
            if (!excludes.contains(key) && value instanceof String) {
                value ? "" : steps.error("Please provide $key")
            }
        }
    }
}
