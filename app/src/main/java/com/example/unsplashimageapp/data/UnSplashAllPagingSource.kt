package com.example.unsplashimageapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashimageapp.data.Entity.responses.UnSplashResponseItem
import com.example.unsplashimageapp.data.api.UnSplashApi
import retrofit2.HttpException
import java.io.IOException

class UnSplashAllPagingSource(private val api:UnSplashApi) : PagingSource<Int, UnSplashResponseItem>()
{

    override fun getRefreshKey(state: PagingState<Int, UnSplashResponseItem>): Int?
    {
        return state.anchorPosition?.let()
        {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?:anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnSplashResponseItem>
    {
        val page = params.key ?: 1



        return try
        {

             val  response  = api.getPhotos(page,params.loadSize)

            LoadResult.Page(
                data = response.body()!!,
                prevKey = if(page==1) null else page-1,
                nextKey = if(response.body().isNullOrEmpty()) null else page+1)

        }catch (e: IOException)
        {
            LoadResult.Error(e)
        } catch (e:HttpException)
        {
            LoadResult.Error(e)
        }

    } // load closed


} // UnSplashPagingSource closed