stage('Build & push images') {
  sh 'make build-kuma push-kuma'
}

stage('Deploy') {
  env.KUBECONFIG = "${env.HOME}/virginia.kube_config"
  env.DEIS_PROFILE = 'virginia'
  env.DEIS_BIN = 'deis2'
  env.DEIS_APP = 'mdn-' + env.BRANCH_NAME

  sh 'make deis-create'
  sh "kubectl --namespace=${env.DEIS_APP} apply -f k8s/"
  sh 'make deis-pull'
  sh 'make k8s-migrate'
  sh 'make deis-scale-worker'
}
