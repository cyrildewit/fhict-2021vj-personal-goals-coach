function docReady(fn) {
    // see if DOM is already available
    if (document.readyState === "complete" || document.readyState === "interactive") {
        // call on next available tick
        setTimeout(fn, 1);
    } else {
        document.addEventListener("DOMContentLoaded", fn);
    }
}

const CreateGoalFormModule = {
    el: {
        createGoalForm: document.getElementById('createGoalForm'),
        titleInput: document.querySelector('#createGoalForm [name=title]'),
        titleHelpTextWrapper: document.querySelector('#createGoalForm #titleHelpTextWrapper'),
        titleHelpText: document.querySelector('#createGoalForm #titleHelpText'),
    },

    init: function () {
        this.bindEventListeners()
    },

    bindEventListeners: function() {
        this.el.titleInput.addEventListener('keydown', this.onTitleKeydownHandler.bind(this))
    },

    onTitleKeydownHandler: function(event) {
        if (event.target.value.length > 130) {
            this.el.titleHelpTextWrapper.classList.remove('d-none');
            this.el.titleHelpText.innerHTML = "title is too long"
        } else {
            this.el.titleHelpTextWrapper.classList.add('d-none');
            this.el.titleHelpText.innerHTML = ""
        }
//        console.log(event)
    },
}

docReady(function () {
    CreateGoalFormModule.init();
})
