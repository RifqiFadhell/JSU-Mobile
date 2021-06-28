package id.jsu.suntiq.utils.base

interface BasePresenterPullToRefresh {

  fun loadFromTheFirstTime()

  fun loadNextPage()

  fun loadFormPullToRefresh()

  fun unbind()

  fun bind()
}