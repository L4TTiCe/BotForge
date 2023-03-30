package com.mohandass.botforge.onboarding.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohandass.botforge.AppRoutes
import com.mohandass.botforge.AppViewModel
import com.mohandass.botforge.onboarding.OnBoardingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoarding(
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel(),
    viewModel: AppViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    val pageCount = 4

    LaunchedEffect(Unit) {
        delay(500)
        pagerState.animateScrollToPage(
            page = 1,
            animationSpec = tween(1500)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            VerticalPager(
                modifier = Modifier.weight(10f),
                pageCount = pageCount,
                state = pagerState,
            ) { position -> when (position) {
                    0 -> OnBoardingUi1()
                    1 -> OnBoardingUi2(
                        onNext = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        }
                    )
                    2 -> OnBoardingUi3(
                        initialApiKey = onBoardingViewModel.getApiKey(),
                        saveApiKey = {
                            onBoardingViewModel.setApiKey(it)
                        },
                        onNext = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(3)
                            }
                        }
                    )
                    3 -> OnBoardingUi4(
                        saveUserGeneratedContent = {
                            onBoardingViewModel.setUserGeneratedContent(it)
                        },
                        onComplete = {
                            onBoardingViewModel.setOnBoardingCompleted()
                            viewModel.navController.navigate(AppRoutes.Main.route) {
                                popUpTo(AppRoutes.OnBoarding.route) { inclusive = true }
                            }
                        }
                    )
                    else -> OnBoardingUi1()
                }
            }
        }
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                // skip indicator for first logo screen
                if (iteration != 0) {
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)

                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun OnBoardingPreview() {
    OnBoarding(viewModel = hiltViewModel())
}
