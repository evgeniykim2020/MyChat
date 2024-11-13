package ru.evgeniykim.mychat.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.GridLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.evgeniykim.mychat.R
import ru.evgeniykim.mychat.ui.App


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhoneText(myText: String) {

    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var phoneCode by rememberSaveable { mutableStateOf("+7") }
    var phoneFlag by rememberSaveable { mutableIntStateOf(R.drawable.ru_flag) }
    var showTelDialog by remember { mutableStateOf(false) }
    var inputIsFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val mask = PhoneMask("(###) ###-##-##")

    Row(
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()) {
            TextButton(onClick = { showTelDialog = true }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = phoneFlag),
                        contentDescription = "ru_flag",
                        modifier = Modifier
                            .size(26.dp)
                            .background(Color.Transparent)
                            .padding(end = 5.dp)
                    )

                    Text(
                        text = phoneCode,
                        modifier = Modifier.padding(end = 5.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto))
                    )
                }
            }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                visualTransformation = mask,
                shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
                label = {
                    Text(text = myText,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = FontFamily(Font(R.font.roboto)))
                },
                placeholder = { Text(text = myText) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                colors = TextFieldDefaults.colors(
                    focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        // possible to add some action

                    }
                )
            )
        }


        if (showTelDialog) {
            AlertDialog(
                onDismissRequest = { showTelDialog = false },
                ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxHeight()
                        ) {
                        TextButton(onClick = {
                            phoneCode = "+7"
                            phoneFlag = R.drawable.ru_flag
                            showTelDialog = false
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ru_flag),
                                    contentDescription = "ru_flag",
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(Color.Transparent)
                                        .padding(end = 5.dp)
                                )
                                Text(
                                    text = "+7",
                                    modifier = Modifier.padding(end = 5.dp),
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto))
                                )
                                Text(
                                    text = "Россия",
                                    modifier = Modifier,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto))
                                )
                            }
                        }
                        TextButton(onClick = {
                            phoneCode = "+375"
                            phoneFlag = R.drawable.bel_flag
                            showTelDialog = false
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.bel_flag),
                                    contentDescription = "belru_flag",
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(Color.Transparent)
                                        .padding(end = 5.dp)
                                )
                                Text(
                                    text = "+375",
                                    modifier = Modifier.padding(end = 5.dp),
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto))
                                )
                                Text(
                                    text = "Беларусь",
                                    modifier = Modifier,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    val userPhone = stringPreferencesKey(Constants.PHONE)
    CoroutineScope(Dispatchers.IO).launch {
        App.applicationContext().dataStore.setValue(userPhone, (phoneCode+text).trim())
    }
}




