package com.fave.breezil.fave.repository


class NetworkState(val status: Status) {
    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED,
        NO_RESULT
    }

    companion object {

        val LOADED: NetworkState
        val LOADING: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS)
            LOADING = NetworkState(Status.RUNNING)
        }
    }
}
