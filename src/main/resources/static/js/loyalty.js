// loyalty.js - рабочая версия

console.log('✅ loyalty.js загружен!');

// ========== ГЛОБАЛЬНЫЕ ПЕРЕМЕННЫЕ ==========
let currentClientId = null;
let currentBonusAmount = 0;
let currentBonusRate = 10;
let timeoutId = null;
let currentQuery = '';

// ========== ЭЛЕМЕНТЫ ==========
const searchInput = document.getElementById('phoneSearch');
const searchButton = document.getElementById('searchButton');
const autocompleteList = document.getElementById('autocompleteResults');
const clientInfo = document.getElementById('clientInfo');
const operationSection = document.getElementById('operationSection');
const emptyState = document.getElementById('emptyState');
const clientName = document.getElementById('clientName');
const clientPhone = document.getElementById('clientPhone');
const clientIdEl = document.getElementById('clientId');
const bonusAmount = document.getElementById('bonusAmount');
const bonusRateDisplay = document.getElementById('bonusRateDisplay');
const purchaseAmount = document.getElementById('purchaseAmount');
const useBonusToggle = document.getElementById('useBonusToggle');
const writeOffSection = document.getElementById('writeOffSection');
const writeOffAmount = document.getElementById('writeOffAmount');
const bonusToEarn = document.getElementById('bonusToEarn');
const availableBonusText = document.getElementById('availableBonusText');
const calcPurchaseAmount = document.getElementById('calcPurchaseAmount');
const calcBonusUsed = document.getElementById('calcBonusUsed');
const calcBonusEarned = document.getElementById('calcBonusEarned');
const calcRemainAmount = document.getElementById('calcRemainAmount');
const paymentInfo = document.getElementById('paymentInfo');
const paymentInfoText = document.getElementById('paymentInfoText');
const noBonusWarning = document.getElementById('noBonusWarning');
const executeButton = document.getElementById('executeButton');
const resetButton = document.getElementById('resetButton');
const operationComment = document.getElementById('operationComment');
const searchError = document.getElementById('searchError');
const errorMessage = document.getElementById('errorMessage');
const searchSuccess = document.getElementById('searchSuccess');
const successMessage = document.getElementById('successMessage');
const operationResult = document.getElementById('operationResult');
const operationSuccess = document.getElementById('operationSuccess');
const operationSuccessMessage = document.getElementById('operationSuccessMessage');
const operationError = document.getElementById('operationError');
const operationErrorMessage = document.getElementById('operationErrorMessage');

// ========== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ==========
function showToast(message, type = 'success') {
    console.log(`[${type}] ${message}`);
    alert(message);
}

function showError(message) {
    if (errorMessage) {
        errorMessage.textContent = message;
        searchError.style.display = 'block';
        searchSuccess.style.display = 'none';
        setTimeout(() => { searchError.style.display = 'none'; }, 5000);
    } else {
        alert('❌ ' + message);
    }
}

function showSuccess(message) {
    if (successMessage) {
        successMessage.textContent = message;
        searchSuccess.style.display = 'block';
        searchError.style.display = 'none';
        setTimeout(() => { searchSuccess.style.display = 'none'; }, 5000);
    } else {
        alert('✅ ' + message);
    }
}

function showOperationSuccess(message) {
    if (operationSuccessMessage) {
        operationSuccessMessage.textContent = message;
        operationSuccess.style.display = 'block';
        operationError.style.display = 'none';
        operationResult.style.display = 'block';
        setTimeout(() => { operationResult.style.display = 'none'; }, 8000);
    } else {
        alert('✅ ' + message);
    }
}

function showOperationError(message) {
    if (operationErrorMessage) {
        operationErrorMessage.textContent = message;
        operationError.style.display = 'block';
        operationSuccess.style.display = 'none';
        operationResult.style.display = 'block';
    } else {
        alert('❌ ' + message);
    }
}

// ========== КАЛЬКУЛЯТОР БОНУСОВ ==========
function calculateBonus() {
    const purchase = parseFloat(purchaseAmount ? purchaseAmount.value : 0) || 0;
    const availableBonus = currentBonusAmount;
    const useBonus = useBonusToggle ? useBonusToggle.checked : false;

    let writeOff = 0;
    if (useBonus) {
        writeOff = parseFloat(writeOffAmount ? writeOffAmount.value : 0) || 0;
        if (writeOff === 0 && purchase > 0) {
            writeOff = Math.min(availableBonus, purchase);
            if (writeOffAmount) writeOffAmount.value = writeOff.toFixed(2);
        }
    } else {
        if (writeOffAmount) writeOffAmount.value = 0;
    }

    const maxWriteOff = Math.min(availableBonus, purchase);
    if (writeOff > maxWriteOff) {
        writeOff = maxWriteOff;
        if (writeOffAmount) writeOffAmount.value = writeOff.toFixed(2);
    }

    const paidByCash = purchase - writeOff;
    const bonusRate = currentBonusRate / 100;
    const bonusEarned = paidByCash > 0.005 ? Math.round(paidByCash * bonusRate * 100) / 100 : 0;

    if (bonusToEarn) bonusToEarn.textContent = `${bonusEarned.toFixed(2)} ₽ (${currentBonusRate}%)`;
    if (calcPurchaseAmount) calcPurchaseAmount.textContent = `${purchase.toFixed(2)} ₽`;
    if (calcBonusUsed) calcBonusUsed.textContent = `${writeOff.toFixed(2)} ₽`;
    if (calcBonusEarned) calcBonusEarned.textContent = `${bonusEarned.toFixed(2)} ₽`;
    if (calcRemainAmount) {
        calcRemainAmount.textContent = `${paidByCash.toFixed(2)} ₽`;
        calcRemainAmount.className = 'remain-amount' + (paidByCash < 0 ? ' negative' : '');
    }

    if (writeOffSection) writeOffSection.style.display = useBonus ? 'block' : 'none';

    if (writeOff > 0 && paidByCash <= 0.005) {
        if (noBonusWarning) noBonusWarning.style.display = 'block';
        const calc = document.querySelector('.bonus-calc');
        if (calc) calc.classList.add('warning');
    } else {
        if (noBonusWarning) noBonusWarning.style.display = 'none';
        const calc = document.querySelector('.bonus-calc');
        if (calc) calc.classList.remove('warning');
    }

    if (paymentInfo && paymentInfoText) {
        paymentInfo.style.display = 'block';
        if (writeOff > 0 && paidByCash > 0.005) {
            paymentInfoText.textContent = `💰 Списано ${writeOff.toFixed(2)} ₽ бонусами. К оплате деньгами: ${paidByCash.toFixed(2)} ₽. Начислено бонусов: ${bonusEarned.toFixed(2)} ₽ (${currentBonusRate}%)`;
        } else if (writeOff > 0 && paidByCash <= 0.005) {
            paymentInfoText.innerHTML = `🎉 Покупка полностью оплачена бонусами! <strong>Бонусы за эту покупку НЕ начисляются.</strong> Остаток бонусов: ${(availableBonus - writeOff).toFixed(2)} ₽`;
        } else {
            paymentInfoText.textContent = `💳 Оплата деньгами: ${purchase.toFixed(2)} ₽. Начислено бонусов: ${bonusEarned.toFixed(2)} ₽ (${currentBonusRate}%)`;
        }
    }
}

// ========== МАКСИМАЛЬНОЕ СПИСАНИЕ ==========
function setMaxWriteOff() {
    const purchase = parseFloat(purchaseAmount ? purchaseAmount.value : 0) || 0;
    const maxWriteOff = Math.min(currentBonusAmount, purchase);
    if (writeOffAmount) writeOffAmount.value = maxWriteOff.toFixed(2);
    calculateBonus();
}
window.setMaxWriteOff = setMaxWriteOff;

// ========== ОБНОВЛЕНИЕ КЛИЕНТА ==========
function updateClientInfo(data) {
    if (data && data.id) {
        if (clientName) clientName.textContent = data.firstName || '-';
        if (clientPhone) clientPhone.textContent = data.phone || '-';
        if (clientIdEl) clientIdEl.textContent = data.id || '-';

        currentBonusRate = data.rate || 10;
        currentBonusAmount = Math.round((data.bonusAmount || 0) * 100) / 100;

        if (bonusAmount) {
            bonusAmount.innerHTML = `${currentBonusAmount.toFixed(2)} ₽`;
            bonusAmount.classList.remove('updated');
            setTimeout(() => bonusAmount.classList.add('updated'), 10);
        }
        if (bonusRateDisplay) bonusRateDisplay.textContent = `${currentBonusRate}%`;
        currentClientId = data.id;

        if (clientInfo) clientInfo.style.display = 'block';
        if (operationSection) operationSection.style.display = 'block';
        if (emptyState) emptyState.style.display = 'none';

        if (availableBonusText) availableBonusText.textContent = `Доступно: ${currentBonusAmount.toFixed(2)} ₽`;
        calculateBonus();
    }
}

// ========== ПОИСК КЛИЕНТА ==========
function findClient() {
    if (!searchInput) {
        alert('Поле поиска не найдено!');
        return;
    }

    const phone = searchInput.value.trim();
    console.log('🔍 Поиск клиента по номеру:', phone);

    if (!phone || phone.length < 2) {
        showError('Введите номер телефона (минимум 2 цифры)');
        return;
    }

    if (searchButton) {
        searchButton.disabled = true;
        searchButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status"></span> Поиск...';
    }

    fetch(`/api/find?phoneNumber=${encodeURIComponent(phone)}`)
        .then(response => {
            console.log('📡 Статус ответа:', response.status);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('✅ Данные клиента:', data);

            if (searchButton) {
                searchButton.disabled = false;
                searchButton.innerHTML = '<i class="bi bi-search"></i> Найти';
            }

            if (data && data.id) {
                updateClientInfo(data);
                showSuccess('Клиент найден!');
            } else {
                if (clientInfo) clientInfo.style.display = 'none';
                if (operationSection) operationSection.style.display = 'none';
                if (emptyState) emptyState.style.display = 'block';
                showError('Клиент не найден');
            }
        })
        .catch(error => {
            console.error('❌ Ошибка:', error);
            if (searchButton) {
                searchButton.disabled = false;
                searchButton.innerHTML = '<i class="bi bi-search"></i> Найти';
            }
            if (clientInfo) clientInfo.style.display = 'none';
            if (operationSection) operationSection.style.display = 'none';
            if (emptyState) emptyState.style.display = 'block';
            showError('Ошибка: ' + error.message);
        });
}

// ========== ВЫПОЛНЕНИЕ ОПЕРАЦИИ ==========
function executeOperation() {
    const purchase = parseFloat(purchaseAmount ? purchaseAmount.value : 0) || 0;
    const useBonus = useBonusToggle ? useBonusToggle.checked : false;
    const writeOff = parseFloat(writeOffAmount ? writeOffAmount.value : 0) || 0;

    if (!currentClientId) {
        showOperationError('Сначала найдите клиента');
        return;
    }

    if (purchase <= 0) {
        showOperationError('Введите сумму покупки');
        if (purchaseAmount) purchaseAmount.focus();
        return;
    }

    const roundedPurchase = Math.round(purchase * 100) / 100;
    const roundedWriteOff = Math.round(writeOff * 100) / 100;
    const paidByCash = Math.round((roundedPurchase - roundedWriteOff) * 100) / 100;

    if (roundedWriteOff > roundedPurchase) {
        showOperationError('Сумма списания не может превышать сумму покупки');
        return;
    }

    if (useBonus && roundedWriteOff > currentBonusAmount) {
        showOperationError(`Недостаточно бонусов. Доступно: ${currentBonusAmount.toFixed(2)} ₽`);
        return;
    }

    if (executeButton) {
        executeButton.disabled = true;
        executeButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status"></span> Обработка...';
    }

    const operations = [];

    if (useBonus && roundedWriteOff > 0) {
        operations.push({
            clientId: currentClientId,
            operationAmount: roundedPurchase,
            bonusAmount: roundedWriteOff,
            operationType: 'WRITE_OFF',
            comment: `Списание бонусов за покупку на ${roundedPurchase.toFixed(2)} ₽`
        });
    }

    if (paidByCash > 0.005) {
        const bonusRate = currentBonusRate / 100;
        const bonusEarned = Math.round(paidByCash * bonusRate * 100) / 100;
        if (bonusEarned > 0) {
            operations.push({
                clientId: currentClientId,
                operationAmount: roundedPurchase,
                bonusAmount: bonusEarned,
                operationType: 'ACCRUAL',
                comment: `Начисление бонусов за покупку (оплачено деньгами: ${paidByCash.toFixed(2)} ₽, ставка: ${currentBonusRate}%)`
            });
        }
    }

    if (operations.length === 0) {
        showOperationError('Нет операций для выполнения');
        if (executeButton) {
            executeButton.disabled = false;
            executeButton.innerHTML = '<i class="bi bi-check-circle"></i> Провести покупку';
        }
        return;
    }

    let operationPromises = operations.map(operation => {
        const endpoint = operation.operationType === 'ACCRUAL' ? '/api/enroll' : '/api/writeOff';
        return fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(operation)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => ({
                ...data,
                operationType: operation.operationType,
                bonusAmount: operation.bonusAmount
            }));
    });

    Promise.all(operationPromises)
        .then(results => {
            if (executeButton) {
                executeButton.disabled = false;
                executeButton.innerHTML = '<i class="bi bi-check-circle"></i> Провести покупку';
            }

            const allSuccess = results.every(r => r && r.newBalance !== undefined);

            if (allSuccess) {
                const newBalance = results[results.length - 1].newBalance;

                // Обновляем данные клиента
                if (clientPhone) {
                    fetch(`/api/find?phoneNumber=${encodeURIComponent(clientPhone.textContent)}`)
                        .then(res => res.json())
                        .then(data => {
                            if (data && data.id) {
                                updateClientInfo(data);
                            }
                        });
                }

                let successMsg = '✅ Покупка успешно оформлена!\n\n';
                results.forEach(result => {
                    if (result.message) {
                        successMsg += result.message + '\n';
                    }
                });
                successMsg += `📊 Новый баланс: ${newBalance.toFixed(2)} ₽`;

                showOperationSuccess(successMsg);

                if (purchaseAmount) purchaseAmount.value = '';
                if (writeOffAmount) writeOffAmount.value = '';
                if (useBonusToggle) useBonusToggle.checked = false;
                calculateBonus();
                if (operationComment) operationComment.value = '';
            } else {
                const errors = results
                    .filter(r => !r || r.newBalance === undefined)
                    .map(r => r?.message || 'Неизвестная ошибка')
                    .join('\n');
                showOperationError('❌ Ошибка при оформлении покупки\n' + errors);
            }
        })
        .catch(error => {
            if (executeButton) {
                executeButton.disabled = false;
                executeButton.innerHTML = '<i class="bi bi-check-circle"></i> Провести покупку';
            }
            showOperationError('❌ Ошибка при оформлении покупки\n' + error.message);
            console.error('Error:', error);
        });
}

// ========== СБРОС ==========
function resetForm() {
    if (searchInput) searchInput.value = '';
    if (clientInfo) clientInfo.style.display = 'none';
    if (operationSection) operationSection.style.display = 'none';
    if (emptyState) emptyState.style.display = 'block';
    currentClientId = null;
    currentBonusAmount = 0;
    currentBonusRate = 10;
    if (purchaseAmount) purchaseAmount.value = '';
    if (writeOffAmount) writeOffAmount.value = '';
    if (useBonusToggle) useBonusToggle.checked = false;
    if (operationComment) operationComment.value = '';
    if (bonusAmount) bonusAmount.innerHTML = '0 ₽';
    if (bonusRateDisplay) bonusRateDisplay.textContent = '10%';
    if (autocompleteList) autocompleteList.style.display = 'none';
    if (operationResult) operationResult.style.display = 'none';
    if (searchError) searchError.style.display = 'none';
    if (searchSuccess) searchSuccess.style.display = 'none';
    if (writeOffSection) writeOffSection.style.display = 'none';
    if (noBonusWarning) noBonusWarning.style.display = 'none';
    const calc = document.querySelector('.bonus-calc');
    if (calc) calc.classList.remove('warning');
    calculateBonus();
}

// ========== АВТОКОМПЛИТ ==========
function showResults(data) {
    if (!autocompleteList) return;

    if (!data || data.length === 0) {
        autocompleteList.innerHTML = `
            <div class="search-result text-muted text-center py-3">
                <i class="bi bi-info-circle"></i> Клиенты не найдены
            </div>
        `;
    } else {
        autocompleteList.innerHTML = data.map(client => `
            <div class="search-result" data-phone="${client.phoneNumber}">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <strong>${client.fullName}</strong>
                        <div class="text-muted small">
                            <i class="bi bi-phone"></i> ${client.phoneNumber}
                        </div>
                    </div>
                    <span class="badge bg-primary">${client.bonusAmount || 0} бонусов</span>
                </div>
            </div>
        `).join('');

        document.querySelectorAll('.search-result').forEach(el => {
            el.addEventListener('click', function() {
                if (searchInput) searchInput.value = this.dataset.phone;
                if (autocompleteList) autocompleteList.style.display = 'none';
                findClient();
            });
        });
    }
    autocompleteList.style.display = 'block';
}

// ========== EVENT LISTENERS ==========
if (searchButton) {
    searchButton.addEventListener('click', function(e) {
        e.preventDefault();
        findClient();
    });
}

if (searchInput) {
    searchInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            findClient();
        }
        if (e.key === 'Escape') {
            if (autocompleteList) autocompleteList.style.display = 'none';
            searchInput.blur();
        }
    });

    searchInput.addEventListener('input', function() {
        clearTimeout(timeoutId);
        const query = this.value.trim();
        currentQuery = query;

        if (query.length < 2) {
            if (autocompleteList) autocompleteList.style.display = 'none';
            return;
        }

        if (autocompleteList) {
            autocompleteList.innerHTML = `
                <div class="text-center p-3 text-muted">
                    <span class="spinner-border spinner-border-sm" role="status"></span>
                    Поиск...
                </div>
            `;
            autocompleteList.style.display = 'block';
        }

        timeoutId = setTimeout(() => {
            fetch(`/api/search?query=${encodeURIComponent(query)}&limit=10`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (currentQuery === query) {
                        showResults(data);
                    }
                })
                .catch(() => {
                    if (autocompleteList) {
                        autocompleteList.innerHTML = `
                            <div class="search-result text-danger text-center py-3">
                                <i class="bi bi-exclamation-triangle"></i> Ошибка загрузки
                            </div>
                        `;
                    }
                });
        }, 300);
    });
}

if (executeButton) {
    executeButton.addEventListener('click', executeOperation);
}

if (purchaseAmount) {
    purchaseAmount.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            executeOperation();
        }
    });
    purchaseAmount.addEventListener('input', function() {
        if (this.value && parseFloat(this.value) < 0) {
            this.classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid');
        }
        calculateBonus();
    });
}

if (resetButton) {
    resetButton.addEventListener('click', resetForm);
}

if (writeOffAmount) {
    writeOffAmount.addEventListener('input', calculateBonus);
}

if (useBonusToggle) {
    useBonusToggle.addEventListener('change', calculateBonus);
}

document.addEventListener('click', function(e) {
    if (!e.target.closest('.position-relative')) {
        if (autocompleteList) autocompleteList.style.display = 'none';
    }
});

// Инициализация
calculateBonus();
console.log('✅ loyalty.js полностью загружен!');